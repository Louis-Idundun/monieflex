package com.sq018.monieflex.services;


import com.sq018.monieflex.exceptions.MonieFlexException;
import com.sq018.monieflex.payloads.vtpass.VtpassDataVariation;
import com.sq018.monieflex.services.providers.VtPassService;
import lombok.RequiredArgsConstructor;
import com.sq018.monieflex.dtos.DataSubscriptionDto;
import com.sq018.monieflex.entities.transactions.Transaction;
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


    public ApiResponse<List<VtpassDataVariation>> viewDataVariations(String code){

        return vtPassService.getDataVariations(code);
    }


    public ApiResponse<String> buyData(DataSubscriptionDto dataSubscriptionDto) {
        String email = UserUtil.getLoginUser();
        var user = userRepository.findByEmailAddress(email).orElseThrow(
                () -> new MonieFlexException("User not found")
        );
        if(userUtil.isBalanceSufficient(BigDecimal.valueOf(dataSubscriptionDto.amount()))) {
            userUtil.updateWalletBalance(BigDecimal.valueOf(dataSubscriptionDto.amount()), true);
            Transaction transaction = new Transaction();
            transaction.setStatus(TransactionStatus.PENDING);
            transaction.setNarration("Electricity Billing");
            transaction.setAccount(dataSubscriptionDto.billersCode());
            transaction.setUser(user);
            transaction.setReference(vtPassService.generateRequestId());
            transaction.setAmount(BigDecimal.valueOf(dataSubscriptionDto.amount()));
            transaction.setTransactionType(TransactionType.BILLS);
            transaction.setBillVariation(dataSubscriptionDto.variationCode().toUpperCase());
            transactionRepository.save(transaction);

            var response = vtPassService.dataSubscription(dataSubscriptionDto, transaction);
            if(response.getReference().equals(transaction.getReference())) {
                if(response.getStatus() == TransactionStatus.FAILED) {
                    userUtil.updateWalletBalance(response.getAmount(), false);
                }
                transactionRepository.save(response);
                return new ApiResponse<>(
                        response.getAccount(),
                        response.getStatus().name()
                );
            } else {
                throw new MonieFlexException("Error in completing transaction");
            }
        } else {
            throw new MonieFlexException("Insufficient balance");
        }
    }
}

