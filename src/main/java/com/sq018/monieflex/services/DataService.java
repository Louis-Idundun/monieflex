package com.sq018.monieflex.services;


import com.sq018.monieflex.enums.BillType;
import com.sq018.monieflex.exceptions.MonieFlexException;
import com.sq018.monieflex.payloads.vtpass.VtpassDataVariation;
import com.sq018.monieflex.services.providers.VtPassService;
import lombok.RequiredArgsConstructor;
import com.sq018.monieflex.dtos.DataSubscriptionDto;
import com.sq018.monieflex.entities.Transaction;
import com.sq018.monieflex.enums.TransactionStatus;
import com.sq018.monieflex.enums.TransactionType;
import com.sq018.monieflex.payloads.ApiResponse;
import com.sq018.monieflex.repositories.TransactionRepository;
import com.sq018.monieflex.repositories.UserRepository;
import com.sq018.monieflex.utils.UserUtil;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DataService {
    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;
    private final UserUtil userUtil;
    private final VtPassService vtPassService;


    public ApiResponse<List<VtpassDataVariation>> viewDataVariations(BillType code) {
        return vtPassService.getDataVariations(code.getType());
    }


    public ApiResponse<String> buyData(DataSubscriptionDto dataSubscriptionDto) {
        String email = UserUtil.getLoginUser();
        var user = userRepository.findByEmailAddress(email).orElseThrow(
                () -> new MonieFlexException("User not found")
        );
        if(userUtil.isBalanceSufficient(BigDecimal.valueOf(dataSubscriptionDto.amount()))) {
            Transaction transaction = new Transaction();
            transaction.setStatus(TransactionStatus.PENDING);
            transaction.setNarration(dataSubscriptionDto.narration());
            transaction.setAccount(dataSubscriptionDto.phone());
            transaction.setUser(user);
            transaction.setReference(vtPassService.generateRequestId());
            transaction.setAmount(BigDecimal.valueOf(dataSubscriptionDto.amount()));
            transaction.setTransactionType(TransactionType.DATA);
            transaction.setBillType(dataSubscriptionDto.type());
            transaction.setBillVariation(dataSubscriptionDto.data().toUpperCase());
            transactionRepository.save(transaction);

            var response = vtPassService.dataSubscription(dataSubscriptionDto, transaction);
            if(response.getReference().equals(transaction.getReference())) {
                transactionRepository.save(response);
                if(response.getStatus() == TransactionStatus.FAILED) {
                    userUtil.updateWalletBalance(response.getAmount(), false);
                    throw new MonieFlexException("Transaction failed");
                } else if(response.getStatus() == TransactionStatus.SUCCESSFUL) {
                    userUtil.updateWalletBalance(response.getAmount(), true);
                    return new ApiResponse<>(response.getAccount(), "Transaction successful");
                } else {
                    throw new MonieFlexException("Transaction pending");
                }
            } else {
                throw new MonieFlexException("Error in completing transaction");
            }
        } else {
            throw new MonieFlexException("Insufficient balance");
        }
    }
}

