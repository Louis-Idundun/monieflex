package com.sq018.monieflex.services;

import com.sq018.monieflex.dtos.TransferDto;
import com.sq018.monieflex.dtos.FLWVerifyAccountDto;
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
import com.sq018.monieflex.repositories.WalletRepository;
import com.sq018.monieflex.services.providers.FlutterwaveService;
import com.sq018.monieflex.utils.UserUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WalletService {
    private final FlutterwaveService flutterwaveService;
    private final TransactionRepository transactionRepository;
    private final WalletRepository walletRepository;
    private final UserUtil userUtil;

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
        if(userUtil.isBalanceSufficient(BigDecimal.valueOf(transfer.amount()))) {
            var bankName = "";
            for(var bank : getAllBanks().getData()) {
                if(bank.getCode().equalsIgnoreCase(transfer.bankCode())) {
                    bankName = bank.getCode();
                }
            }

            Transaction transaction = new Transaction();
            transaction.setAccount(transfer.accountNumber());
            transaction.setNarration(transfer.narration());
            transaction.setAmount(BigDecimal.valueOf(transfer.amount()));
            transaction.setReference(generateTxRef());
            transaction.setTransactionType(TransactionType.EXTERNAL_TRANSFER);
            transaction.setStatus(TransactionStatus.PENDING);
            transaction.setReceivingBankName(bankName);
            transaction.setReceivingBankCode(transfer.bankCode());
            transactionRepository.save(transaction);

            userUtil.updateWalletBalance(BigDecimal.valueOf(transfer.amount()), true);

            var result = flutterwaveService.bankTransfer(transfer, transaction.getReference());
            ApiResponse<String> response = new ApiResponse<>();
            if(transaction.getStatus() == TransactionStatus.SUCCESSFUL
                    && result.getReference().equals(transaction.getReference())) {
                transaction.setStatus(TransactionStatus.SUCCESSFUL);
                transactionRepository.save(transaction);
                userUtil.updateWalletBalance(BigDecimal.valueOf(transfer.amount()), false);
                response.setStatus(HttpStatus.OK);
                response.setMessage("Transaction Successful");
                response.setData("Transfer to %s successful".formatted(transaction.getAccount()));
            } else {
                transaction.setStatus(TransactionStatus.FAILED);
                transactionRepository.save(transaction);
                userUtil.updateWalletBalance(BigDecimal.valueOf(transfer.amount()), false);
                response.setStatus(HttpStatus.OK);
                response.setMessage("Transaction Failed");
                response.setData("Couldn't process transaction");
            }
            return response;
        } else {
            throw new MonieFlexException("Insufficient Balance");
        }
    }

    public ApiResponse<List<AllBanksData>> getAllBanks(){
        return flutterwaveService.getAllBanks();
    }

    public ApiResponse<VerifyAccountResponse> verifyBankAccount(FLWVerifyAccountDto accountDto) {
        return flutterwaveService.verifyBankAccount(accountDto);
    }

    public ApiResponse<List<TransactionHistoryResponse>> queryHistory() {
        String email = UserUtil.getLoginUser();
        var transactions = transactionRepository.findByUser_EmailAddress(email);
        List<TransactionHistoryResponse> history = new ArrayList<>();
        transactions.forEach(transaction -> {
            TransactionHistoryResponse response = new TransactionHistoryResponse();
            response.setAccount(transaction.getAccount());
            response.setId(transaction.getId());
            response.setAmount(transaction.getAmount());
            response.setStatus(transaction.getStatus());
            response.setBillType(transaction.getBillType());
            response.setProviderReference(transaction.getProviderReference());
            response.setTransactionType(transaction.getTransactionType());
            response.setReceivingBankName(transaction.getReceivingBankName());
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
}
