package com.sq018.monieflex.services;

import com.sq018.monieflex.dtos.*;
import com.sq018.monieflex.entities.User;
import com.sq018.monieflex.entities.Wallet;
import com.sq018.monieflex.entities.Transaction;
import com.sq018.monieflex.enums.TransactionStatus;
import com.sq018.monieflex.enums.TransactionType;
import com.sq018.monieflex.exceptions.MonieFlexException;
import com.sq018.monieflex.payloads.ApiResponse;
import com.sq018.monieflex.payloads.TransactionDataResponse;
import com.sq018.monieflex.payloads.TransactionHistoryResponse;
import com.sq018.monieflex.payloads.WalletPayload;
import com.sq018.monieflex.payloads.flutterwave.VerifyAccountResponse;
import com.sq018.monieflex.payloads.flutterwave.AllBanksData;
import com.sq018.monieflex.repositories.TransactionRepository;
import com.sq018.monieflex.repositories.UserRepository;
import com.sq018.monieflex.repositories.WalletRepository;
import com.sq018.monieflex.services.providers.FlutterwaveService;
import com.sq018.monieflex.utils.TimeUtils;
import com.sq018.monieflex.utils.UserUtil;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WalletService {
    private final FlutterwaveService flutterwaveService;
    private final TransactionRepository transactionRepository;
    private final WalletRepository walletRepository;
    private final UserUtil userUtil;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private final Integer AVERAGE = 1000;

    protected String generateTxRef() {
        return "MONF-" + UUID.randomUUID().toString().substring(0, 6);
    }

    public Wallet create(User user) {
        Wallet wallet = flutterwaveService.createWallet(
                user.getEmailAddress(), user.getBvn(), generateTxRef(),
                user.getLastName(), user.getFirstName(), user.getPhoneNumber()
        );
        wallet.setUser(user);
        return wallet;
    }

    public ApiResponse<String> transferToBank(TransferDto transfer) {
        String loginUserEmail = UserUtil.getLoginUser();
        User user = userRepository.findByEmailAddress(loginUserEmail).orElseThrow();
        if(userUtil.isBalanceSufficient(BigDecimal.valueOf(transfer.amount()))) {
            Transaction transaction = new Transaction();
            transaction.setAccount(transfer.accountNumber());
            transaction.setNarration(transfer.narration());
            transaction.setAmount(BigDecimal.valueOf(transfer.amount()));
            transaction.setReference(generateTxRef());
            transaction.setTransactionType(TransactionType.EXTERNAL);
            transaction.setStatus(TransactionStatus.PENDING);
            transaction.setReceivingBankName(transfer.bankName());
            transaction.setReceiverName(transfer.receiverName());
            transaction.setReceivingBankCode(transfer.bankCode());
            transaction.setUser(user);
            transactionRepository.save(transaction);

            userUtil.updateWalletBalance(BigDecimal.valueOf(transfer.amount()), true);

            var result = flutterwaveService.bankTransfer(transfer, transaction.getReference());
            if(result.getStatus() == TransactionStatus.SUCCESSFUL) {
                transaction.setStatus(TransactionStatus.SUCCESSFUL);
                transactionRepository.save(transaction);
                userUtil.updateWalletBalance(BigDecimal.valueOf(transfer.amount()), false);
                return new ApiResponse<>("Transaction successful", HttpStatus.OK);
            } else {
                transaction.setStatus(TransactionStatus.FAILED);
                transactionRepository.save(transaction);
                userUtil.updateWalletBalance(BigDecimal.valueOf(transfer.amount()), false);
                return new ApiResponse<>("Transaction failed", HttpStatus.BAD_REQUEST);
            }
        } else {
            throw new MonieFlexException("Insufficient Balance");
        }
    }

    public ApiResponse<?> localTransfer(LocalTransferRequest localTransferRequest){
        String loginUserEmail = UserUtil.getLoginUser();
        User user = userRepository.findByEmailAddress(loginUserEmail).orElse(null);
        if (Objects.isNull(user)){
            return new ApiResponse<>("Invalid Request", HttpStatus.BAD_REQUEST, 11);
        }
        if (!userUtil.isBalanceSufficient(localTransferRequest.getAmount())){
            return new ApiResponse<>("Insufficient Balance to complete this transaction", HttpStatus.BAD_REQUEST, 11);
        }

        Transaction transaction = new Transaction();
        transaction.setAccount(localTransferRequest.getAccountNumber());
        transaction.setNarration(localTransferRequest.getNarration());
        transaction.setAmount(localTransferRequest.getAmount());
        transaction.setReference(generateTxRef());
        transaction.setReceiverName(localTransferRequest.getReceiverName());
        transaction.setTransactionType(TransactionType.LOCAL);
        transaction.setStatus(TransactionStatus.PENDING);
        transaction.setReceivingBankName("Monieflex");
        transactionRepository.save(transaction);

        //todo restructure transaction table to cover debit and credit types
        Wallet wallet = walletRepository.findByNumber(localTransferRequest.getAccountNumber()).orElse(null);
        if (Objects.isNull(wallet)){
            transaction.setStatus(TransactionStatus.FAILED);
            transactionRepository.save(transaction);
            return new ApiResponse<>("Invalid Request", HttpStatus.BAD_REQUEST, 11);
        }
        userUtil.updateWalletBalance(localTransferRequest.getAmount(), true);
        BigDecimal walletBalance = wallet.getBalance();
        BigDecimal newWalletBalance = walletBalance.add(localTransferRequest.getAmount());
        wallet.setBalance(newWalletBalance);
        walletRepository.save(wallet);

        transaction.setStatus(TransactionStatus.SUCCESSFUL);
        transactionRepository.save(transaction);

        return new ApiResponse<>("Transfer Successful", HttpStatus.OK, 1);
    }

    public ApiResponse<List<AllBanksData>> getAllBanks(){
        return flutterwaveService.getAllBanks();
    }

    public ApiResponse<VerifyAccountResponse> verifyBankAccount(FLWVerifyAccountDto accountDto) {
        return flutterwaveService.verifyBankAccount(accountDto);
    }

    public ApiResponse<LocalAccountQueryResponse> queryLocalAccount(LocalAccountQueryRequest localAccountQueryRequest){
        List<Object[]> user = userRepository.findUserByWalletNumber(localAccountQueryRequest.getAccount());
        if (ObjectUtils.isEmpty(user)){
            return new ApiResponse<>("Invalid account", HttpStatus.BAD_REQUEST);
        }
        LocalAccountQueryResponse localAccountQueryResponse = new LocalAccountQueryResponse();
        localAccountQueryResponse.setName(user.get(0)[0] + " " + user.get(0)[1]);
        return new ApiResponse<>(localAccountQueryResponse, "Success", HttpStatus.OK);
    }

    public ApiResponse<List<TransactionHistoryResponse>> queryHistory(Integer page, Integer size) {
        String email = UserUtil.getLoginUser();
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.desc("createdAt")));
        var transactions = transactionRepository.findByUser_EmailAddress(email, pageable);
        List<TransactionHistoryResponse> history = new ArrayList<>();
        transactions.forEach(transaction -> history.add(prepareTransactionHistory(transaction)));
        return new ApiResponse<>(history, "Transaction History successfully fetched");
    }

    private String getName(Transaction transaction) {
        if(transaction.getTransactionType() == TransactionType.AIRTIME
            || transaction.getTransactionType() == TransactionType.DATA
        ) {
            return transaction.getAccount();
        } else if(transaction.getTransactionType() == TransactionType.TV) {
            return transaction.getAccount();
        } else if(transaction.getTransactionType() == TransactionType.ELECTRICITY) {
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            String date = transaction.getCreatedAt().format(timeFormatter);
            return date + "/" + transaction.getAccount().substring(transaction.getAccount().length() - 5);
        } else {
            return transaction.getReceiverName();
        }
    }

    private String getDescription(Transaction transaction) {
        if(transaction.getTransactionType() == TransactionType.AIRTIME) {
            return transaction.getReceiverName();
        } else if(transaction.getTransactionType() == TransactionType.TV) {
            return transaction.getBillVariation().toUpperCase();
        } else if(transaction.getTransactionType() == TransactionType.ELECTRICITY) {
            return transaction.getBillType().name().toUpperCase();
        } else if(transaction.getTransactionType() == TransactionType.DATA) {
            return transaction.getBillVariation().toUpperCase();
        } else {
            return transaction.getNarration();
        }
    }

    public static String getTime(LocalDateTime dateTime) {
        LocalDateTime currentDateTime = LocalDateTime.now();

        if (dateTime.toLocalDate().equals(currentDateTime.toLocalDate())) {
            return "Today";
        } else if (dateTime.toLocalDate().equals(currentDateTime.minusDays(1).toLocalDate())) {
            return "Yesterday";
        } else {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("h:mma");
            return dateTime.format(formatter);
        }
    }

    public static String getDate(LocalDateTime dateTime) {
        LocalDateTime currentDateTime = LocalDateTime.now();

        if (dateTime.toLocalDate().equals(currentDateTime.toLocalDate())) {
            return "Today";
        } else if (dateTime.toLocalDate().equals(currentDateTime.minusDays(1).toLocalDate())) {
            return "Yesterday";
        } else {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            return dateTime.format(formatter);
        }
    }

    public static String getAmount(BigDecimal n) {
        return NumberFormat.getCurrencyInstance().format(n);
    }

    private TransactionHistoryResponse prepareTransactionHistory(Transaction transaction) {
        String loginUserEmail = UserUtil.getLoginUser();
        Wallet user = walletRepository.findByUser_EmailAddressIgnoreCase(loginUserEmail)
                .orElseThrow(() -> new MonieFlexException("User not found"));

        TransactionHistoryResponse response = new TransactionHistoryResponse();
        response.setName(getName(transaction));
        response.setDescription(getDescription(transaction));
        response.setTime(getTime(transaction.getCreatedAt()));
        response.setDate(getDate(transaction.getCreatedAt()));
        response.setAmount(getAmount(transaction.getAmount()));
        response.setIsCredit(user.getNumber().equals(transaction.getAccount()));
        return response;
    }

    public ApiResponse<List<TransactionHistoryResponse>> queryHistory(Integer page, Integer size, TransactionType type) {
        String email = UserUtil.getLoginUser();
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.desc("createdAt")));
        var transactions = transactionRepository.findByTransactionTypeAndUser_EmailAddress(type, email, pageable);
        List<TransactionHistoryResponse> history = new ArrayList<>();
        transactions.forEach(transaction -> history.add(prepareTransactionHistory(transaction)));
        return new ApiResponse<>(history, "Transaction History successfully fetched");
    }

    public ApiResponse<WalletPayload> queryWalletDetails() {
        String email = UserUtil.getLoginUser();
        var wallet = walletRepository.findByUser_EmailAddressIgnoreCase(email).orElseThrow(
                () -> new MonieFlexException("Invalid user id")
        );
        WalletPayload payload = new WalletPayload();
        payload.setBalance(wallet.getBalance());
        payload.setNumber(wallet.getNumber());
        payload.setBankName(wallet.getBankName());

        return new ApiResponse<>(payload, "Wallet successfully fetched");
    }

    public ApiResponse<String> createTransactionPin(String pin) {
        String email = UserUtil.getLoginUser();
        var user = userRepository.findByEmailAddress(email).orElseThrow(
                () -> new MonieFlexException("User not found")
        );
        if(pin.length() < 4) {
            throw new MonieFlexException("Pin cannot be less than 4");
        }
        if(pin.length() > 4) {
            throw new MonieFlexException("Pin cannot be more than 4");
        }
        if(user.getTransactionPin() != null) {
            throw new MonieFlexException("You already have a transaction pin.");
        }
        user.setTransactionPin(passwordEncoder.encode(pin));
        userRepository.save(user);
        return new ApiResponse<>("Pin saved successfully", HttpStatus.OK);
    }

    public ApiResponse<String> verifyPin(String pin) {
        String email = UserUtil.getLoginUser();
        var user = userRepository.findByEmailAddress(email).orElseThrow(
                () -> new MonieFlexException("User not found")
        );
        if(user.getTransactionPin() == null) {
            throw new MonieFlexException("You have not created a pin yet");
        } else {
            if(passwordEncoder.matches(pin, user.getTransactionPin())) {
                return new ApiResponse<>("Pin matches", HttpStatus.OK);
            } else {
                throw new MonieFlexException("Pin does not match");
            }
        }
    }

    public ApiResponse<List<TransactionDataResponse>> getTransactionChart() {
        var transactions = transactionRepository.queryByUser_EmailAddress(UserUtil.getLoginUser());
        var months = TimeUtils.getMonths();

        List<Transaction> month1 = new ArrayList<>();
        List<Transaction> month2 = new ArrayList<>();
        List<Transaction> month3 = new ArrayList<>();
        List<Transaction> month4 = new ArrayList<>();
        List<Transaction> month5 = new ArrayList<>();
        List<Transaction> month6 = new ArrayList<>();
        List<Transaction> month7 = new ArrayList<>();
        List<Transaction> month8 = new ArrayList<>();

        List<TransactionDataResponse> list = new ArrayList<>();
        transactions.forEach(transaction -> {
            if(transaction.getCreatedAt().getMonth().name().equals(months.get(0))) {
                month1.add(transaction);
            } else if(transaction.getCreatedAt().getMonth().name().equals(months.get(1))) {
                month2.add(transaction);
            } else if(transaction.getCreatedAt().getMonth().name().equals(months.get(2))) {
                month3.add(transaction);
            } else if(transaction.getCreatedAt().getMonth().name().equals(months.get(3))) {
                month4.add(transaction);
            } else if(transaction.getCreatedAt().getMonth().name().equals(months.get(4))) {
                month5.add(transaction);
            } else if(transaction.getCreatedAt().getMonth().name().equals(months.get(5))) {
                month6.add(transaction);
            } else if(transaction.getCreatedAt().getMonth().name().equals(months.get(6))) {
                month7.add(transaction);
            } else if(transaction.getCreatedAt().getMonth().name().equals(months.get(7))) {
                month8.add(transaction);
            }
        });

        var month1Response = prepareChart(month1, months.get(0));
        list.add(month1Response);

        var month2Response = prepareChart(month2, months.get(1));
        list.add(month2Response);

        var month3Response = prepareChart(month3, months.get(2));
        list.add(month3Response);

        var month4Response = prepareChart(month4, months.get(3));
        list.add(month4Response);

        var month5Response = prepareChart(month5, months.get(4));
        list.add(month5Response);

        var month6Response = prepareChart(month6, months.get(5));
        list.add(month6Response);

        var month7Response = prepareChart(month7, months.get(6));
        list.add(month7Response);

        var month8Response = prepareChart(month8, months.get(7));
        list.add(month8Response);

        return new ApiResponse<>(list, "Data fetched successfully", HttpStatus.OK);
    }

    private TransactionDataResponse prepareChart(List<Transaction> transactions, String month) {
        Wallet user = walletRepository.findByUser_EmailAddressIgnoreCase(UserUtil.getLoginUser())
                .orElseThrow(() -> new MonieFlexException("User not found"));

        List<BigDecimal> incomeList = new ArrayList<>();
        List<BigDecimal> expenseList = new ArrayList<>();

        transactions.forEach(transaction -> {
            var isCredit = user.getNumber().equals(transaction.getAccount());
            if(isCredit) {
                incomeList.add(transaction.getAmount());
            } else {
                expenseList.add(transaction.getAmount());
            }
        });

        var income = calculateExpenditure(incomeList);
        var expense = calculateExpenditure(expenseList);
        TransactionDataResponse response = new TransactionDataResponse();
        response.setIncome(income);
        response.setExpense(expense);
        response.setMonth(month.substring(0, 3));
        return response;
    }

    private String calculateExpenditure(List<BigDecimal> list) {
        double total = 0.0;

        for (BigDecimal bigDecimal : list) {
            total += Double.parseDouble(String.valueOf(bigDecimal));
        }
        var data = total * 30;
        var result = data / AVERAGE;
        return String.valueOf(result);
    }
}
