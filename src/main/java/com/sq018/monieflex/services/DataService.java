package com.sq018.monieflex.services;


import com.sq018.monieflex.exceptions.MonieFlexException;
import com.sq018.monieflex.payloads.vtpass.VtpassDataVariationResponse;
import com.sq018.monieflex.services.providers.VtPassService;
import com.sq018.monieflex.utils.VtpassEndpoints;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import com.sq018.monieflex.dtos.DataSubscriptionDto;
import com.sq018.monieflex.entities.transactions.Transaction;
import com.sq018.monieflex.enums.TransactionStatus;
import com.sq018.monieflex.enums.TransactionType;
import com.sq018.monieflex.payloads.ApiResponse;
import com.sq018.monieflex.repositories.TransactionRepository;
import com.sq018.monieflex.repositories.UserRepository;
import com.sq018.monieflex.utils.UserUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class DataService {


    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;
    private final UserUtil userUtil;
    private final VtPassService vtPassService;
    private final RestTemplate restTemplate;


    public VtpassDataVariationResponse getDataVariations(String code){
        HttpEntity<Object> entity = new HttpEntity<>(vtPassService.vtPassGetHeader());
        var response = restTemplate.exchange(
                VtpassEndpoints.VARIATION_URL(code), HttpMethod.GET, entity, VtpassDataVariationResponse.class
        );
        if(response.getStatusCode().is2xxSuccessful()){
            return response.getBody();
        } else {
            throw new MonieFlexException("Request failed");
        }
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

