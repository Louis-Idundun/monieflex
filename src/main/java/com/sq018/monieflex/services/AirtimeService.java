package com.sq018.monieflex.services;

import com.sq018.monieflex.dtos.AirtimeDto;
import com.sq018.monieflex.entities.Transaction;
import com.sq018.monieflex.enums.TransactionStatus;
import com.sq018.monieflex.enums.TransactionType;
import com.sq018.monieflex.exceptions.MonieFlexException;
import com.sq018.monieflex.payloads.ApiResponse;
import com.sq018.monieflex.repositories.TransactionRepository;
import com.sq018.monieflex.repositories.UserRepository;
import com.sq018.monieflex.services.providers.VtPassService;
import com.sq018.monieflex.utils.UserUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class AirtimeService {
    private final VtPassService vtPassService;
    private final UserRepository userRepository;
    private final UserUtil userUtil;
    private final TransactionRepository transactionRepository;

    public ApiResponse<String> buyAirtime(AirtimeDto airtimeDto) {
        String email = UserUtil.getLoginUser();
        var user = userRepository.findByEmailAddress(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if(userUtil.isBalanceSufficient(BigDecimal.valueOf(airtimeDto.amount()))) {
            System.out.println(vtPassService.generateRequestId());
            Transaction transaction = new Transaction();
            transaction.setStatus(TransactionStatus.PENDING);
            transaction.setAmount(BigDecimal.valueOf(airtimeDto.amount()));
            transaction.setAccount(airtimeDto.phoneNumber());
            transaction.setBillType(airtimeDto.network());
            transaction.setTransactionType(TransactionType.AIRTIME);
            transaction.setUser(user);
            transaction.setReceiverName(airtimeDto.beneficiaryName());
            transaction.setReference(vtPassService.generateRequestId());
            transaction.setNarration(airtimeDto.narration());
            transactionRepository.save(transaction);

            userUtil.updateWalletBalance(BigDecimal.valueOf(airtimeDto.amount()), true);

            var vtResponse = vtPassService.buyAirtime(airtimeDto, transaction);
            transactionRepository.save(vtResponse);

            ApiResponse<String> response = new ApiResponse<>();
            if(vtResponse.getStatus() == TransactionStatus.FAILED) {
                userUtil.updateWalletBalance(BigDecimal.valueOf(airtimeDto.amount()), false);
                response.setStatus(HttpStatus.BAD_REQUEST);
                response.setStatusCode(400);
                response.setData(vtResponse.getStatus().toString());
                response.setMessage("Couldn't complete transaction");
            } else {
                response.setStatus(HttpStatus.OK);
                response.setStatusCode(200);
                response.setData(vtResponse.getStatus().toString());
                response.setMessage("Transaction completed");
            }
            return response;
        } else {
            throw new MonieFlexException("Insufficient balance");
        }
    }
}
