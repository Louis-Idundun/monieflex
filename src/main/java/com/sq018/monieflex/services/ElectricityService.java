package com.sq018.monieflex.services;

import com.sq018.monieflex.dtos.ElectricityDto;
import com.sq018.monieflex.dtos.VerifyMeterDto;
import com.sq018.monieflex.entities.Transaction;
import com.sq018.monieflex.enums.TransactionStatus;
import com.sq018.monieflex.enums.TransactionType;
import com.sq018.monieflex.exceptions.MonieFlexException;
import com.sq018.monieflex.payloads.ApiResponse;
import com.sq018.monieflex.payloads.vtpass.VtPassVerifyMeterContent;
import com.sq018.monieflex.repositories.TransactionRepository;
import com.sq018.monieflex.repositories.UserRepository;
import com.sq018.monieflex.services.providers.VtPassService;
import com.sq018.monieflex.utils.UserUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class ElectricityService {

    private final VtPassService vtPassService;
    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;
    private final UserUtil userUtil;

    public ApiResponse<String> buyElectricity(ElectricityDto electricityDto) {
        String email = UserUtil.getLoginUser();
        var user = userRepository.findByEmailAddress(email).orElseThrow(
                () -> new MonieFlexException("User not found")
        );
        if(userUtil.isBalanceSufficient(BigDecimal.valueOf(electricityDto.amount()))) {
            userUtil.updateWalletBalance(BigDecimal.valueOf(electricityDto.amount()), true);
            Transaction transaction = new Transaction();
            transaction.setStatus(TransactionStatus.PENDING);
            transaction.setNarration(electricityDto.narration());
            transaction.setAccount(electricityDto.meterNumber());
            transaction.setUser(user);
            transaction.setReference(vtPassService.generateRequestId());
            transaction.setAmount(BigDecimal.valueOf(electricityDto.amount()));
            transaction.setTransactionType(TransactionType.ELECTRICITY);
            transaction.setBillType(electricityDto.type());
            transactionRepository.save(transaction);

            var response = vtPassService.electricitySubscription(electricityDto, transaction);
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
    public ApiResponse<VtPassVerifyMeterContent> queryElectricityAccount(VerifyMeterDto verifyMeter) {
        return vtPassService.queryElectricityAccount(verifyMeter);
    }
}
