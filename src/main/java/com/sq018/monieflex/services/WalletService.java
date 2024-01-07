package com.sq018.monieflex.services;

import com.sq018.monieflex.dtos.*;
import com.sq018.monieflex.entities.account.User;
import com.sq018.monieflex.entities.account.Wallet;
import com.sq018.monieflex.entities.transactions.Transaction;
import com.sq018.monieflex.enums.TransactionStatus;
import com.sq018.monieflex.enums.TransactionType;
import com.sq018.monieflex.exceptions.MonieFlexException;
import com.sq018.monieflex.payloads.ApiResponse;
import com.sq018.monieflex.payloads.TransactionHistoryResponse;
import com.sq018.monieflex.payloads.WalletPayload;
import com.sq018.monieflex.payloads.flutterwave.VerifyAccountResponse;
import com.sq018.monieflex.payloads.flutterwave.AllBanksData;
import com.sq018.monieflex.repositories.TransactionRepository;
import com.sq018.monieflex.repositories.UserRepository;
import com.sq018.monieflex.repositories.WalletRepository;
import com.sq018.monieflex.services.providers.FlutterwaveService;
import com.sq018.monieflex.utils.CreditCardUtils;
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
    private static final BigDecimal MINIMUM_FUND_AMOUNT = new BigDecimal("100.0");

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
        User user = userRepository.findByEmailAddress(loginUserEmail).orElseThrow(
                () -> new MonieFlexException("User not found")
        );
        if (!userUtil.isBalanceSufficient(localTransferRequest.getAmount())){
            return new ApiResponse<>("Insufficient Balance", HttpStatus.BAD_REQUEST);
        }

        Transaction transaction = new Transaction();
        transaction.setAccount(localTransferRequest.getAccountNumber());
        transaction.setNarration(localTransferRequest.getNarration());
        transaction.setAmount(localTransferRequest.getAmount());
        transaction.setReference(generateTxRef());
        transaction.setReceiverName(localTransferRequest.getReceiverName());
        transaction.setTransactionType(TransactionType.LOCAL);
        transaction.setStatus(TransactionStatus.PENDING);
        transaction.setReceivingBankName("MonieFlex");
        transaction.setUser(user);
        transactionRepository.save(transaction);

        var wallet = walletRepository.findByNumber(localTransferRequest.getAccountNumber());
        if (wallet.isPresent()){
            userUtil.updateWalletBalance(localTransferRequest.getAmount(), true);
            wallet.get().setBalance(wallet.get().getBalance().add(localTransferRequest.getAmount()));
            transaction.setStatus(TransactionStatus.SUCCESSFUL);
            transactionRepository.save(transaction);
            return new ApiResponse<>("Transfer Successful", HttpStatus.OK);
        } else {
            transaction.setStatus(TransactionStatus.FAILED);
            transactionRepository.save(transaction);
            return new ApiResponse<>("Transaction failed", HttpStatus.BAD_REQUEST);
        }
    }

    public ApiResponse<List<AllBanksData>> getAllBanks(){
        return flutterwaveService.getAllBanks();
    }

    public ApiResponse<VerifyAccountResponse> verifyBankAccount(FLWVerifyAccountDto accountDto) {
        return flutterwaveService.verifyBankAccount(accountDto);
    }

    public ApiResponse<LocalAccountQueryResponse> queryLocalAccount(LocalAccountQueryRequest localAccountQueryRequest){
        var wallet = walletRepository.findByNumber(localAccountQueryRequest.getAccount())
                .orElseThrow(() -> new MonieFlexException("Invalid Account"));
        var userWallet = walletRepository.findByUser_EmailAddressIgnoreCase(UserUtil.getLoginUser())
                .orElseThrow(() -> new MonieFlexException("User not found"));

        if(localAccountQueryRequest.getAccount().equals(userWallet.getNumber())) {
            throw new MonieFlexException("You cannot transfer money from your wallet to your wallet");
        }

        var user = wallet.getUser();
        LocalAccountQueryResponse localAccountQueryResponse = new LocalAccountQueryResponse();
        localAccountQueryResponse.setName(user.getFirstName() + " " + user.getLastName());
        return new ApiResponse<>(localAccountQueryResponse, "Success", HttpStatus.OK);
    }

    public ApiResponse<List<TransactionHistoryResponse>> queryHistory(Integer page, Integer size) {
        String email = UserUtil.getLoginUser();
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.desc("createdAt")));
        var transactions = transactionRepository.findByUser_EmailAddress(email, pageable);
        List<TransactionHistoryResponse> history = new ArrayList<>();
        transactions.forEach(transaction -> {
            TransactionHistoryResponse response = new TransactionHistoryResponse();
            response.setAccount(transaction.getAccount());
            response.setId(transaction.getId());
            response.setAmount(transaction.getAmount());
            response.setStatus(transaction.getStatus());
            response.setBillType(transaction.getBillType());
            response.setReceiverName(transaction.getReceiverName());
            response.setProviderReference(transaction.getProviderReference());
            response.setTransactionType(transaction.getTransactionType());
            response.setReceivingBankName(transaction.getReceivingBankName());
            response.setNarration(transaction.getNarration());
            response.setCreatedAt(transaction.getCreatedAt());
            response.setVariation(transaction.getBillVariation());
            history.add(response);
        });
        return new ApiResponse<>(history, "Transaction History successfully fetched");
    }

    public ApiResponse<List<TransactionHistoryResponse>> queryHistory(Integer page, Integer size, TransactionType type) {
        String email = UserUtil.getLoginUser();
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.desc("createdAt")));
        var transactions = transactionRepository.findByTransactionTypeAndUser_EmailAddress(type, email, pageable);
        List<TransactionHistoryResponse> history = new ArrayList<>();
        transactions.forEach(transaction -> {
            TransactionHistoryResponse response = new TransactionHistoryResponse();
            response.setAccount(transaction.getAccount());
            response.setId(transaction.getId());
            response.setAmount(transaction.getAmount());
            response.setStatus(transaction.getStatus());
            response.setBillType(transaction.getBillType());
            response.setReceiverName(transaction.getReceiverName());
            response.setProviderReference(transaction.getProviderReference());
            response.setTransactionType(transaction.getTransactionType());
            response.setReceivingBankName(transaction.getReceivingBankName());
            response.setNarration(transaction.getNarration());
            response.setVariation(transaction.getBillVariation());
            response.setCreatedAt(transaction.getCreatedAt());
            history.add(response);
        });
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

    public ApiResponse<String> fundWallet(FundWalletDto fundWalletDto) {
        var card = CreditCardUtils.verify(() -> fundWalletDto).orElseThrow();

        if (card.getAmount().compareTo(MINIMUM_FUND_AMOUNT) < 0) {
            throw new MonieFlexException("Amount to fund must be at least " + MINIMUM_FUND_AMOUNT);
        }

        String email = UserUtil.getLoginUser();
        var user = userRepository.findByEmailAddress(email).orElseThrow(
                () -> new MonieFlexException("User not found")
        );
        var wallet = walletRepository.findByUser_EmailAddressIgnoreCase(email).orElseThrow(
                () -> new MonieFlexException("User not found")
        );

        Transaction transaction = new Transaction();
        transaction.setAmount(fundWalletDto.getAmount());
        transaction.setUser(user);
        transaction.setAccount(wallet.getNumber());
        transaction.setNarration("Fund Wallet");
        transaction.setReference(fundWalletDto.getCardNumber());
        transaction.setReceiverName(user.getFirstName() + " " + user.getLastName());
        transaction.setTransactionType(TransactionType.EXTERNAL);
        transaction.setStatus(TransactionStatus.SUCCESSFUL);
        transaction.setReceivingBankName("MonieFlex");
        transactionRepository.save(transaction);

        userUtil.updateWalletBalance(fundWalletDto.getAmount(), false);

        return new ApiResponse<>("Transaction successful", HttpStatus.OK);
    }

}
