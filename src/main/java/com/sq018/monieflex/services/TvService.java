package com.sq018.monieflex.services;
import com.sq018.monieflex.dtos.VerifySmartCard;
import com.sq018.monieflex.dtos.TvSubsDto;
import com.sq018.monieflex.entities.Transaction;
import com.sq018.monieflex.enums.BillType;
import com.sq018.monieflex.enums.TransactionStatus;
import com.sq018.monieflex.enums.TransactionType;
import com.sq018.monieflex.exceptions.MonieFlexException;
import com.sq018.monieflex.payloads.ApiResponse;
import com.sq018.monieflex.payloads.vtpass.TvSubscriptionQueryContent;
import com.sq018.monieflex.payloads.vtpass.VtpassTVariation;
import com.sq018.monieflex.repositories.TransactionRepository;
import com.sq018.monieflex.repositories.UserRepository;
import com.sq018.monieflex.services.providers.VtPassService;
import com.sq018.monieflex.utils.UserUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TvService {
    private final VtPassService vtPassService;
    private final UserUtil userUtil;
    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;

    public ApiResponse<List<VtpassTVariation>> viewTvVariations(BillType code) {
        return vtPassService.getTvVariations(code.getType());
    }

    public ApiResponse<TvSubscriptionQueryContent> queryTvAccount(VerifySmartCard smartCard) {
        return vtPassService.queryTVAccount(smartCard);
    }

    public ApiResponse<String> payTvSubscription(TvSubsDto tvSubsDto) {
        String email = UserUtil.getLoginUser();
        var user = userRepository.findByEmailAddress(email).orElseThrow(
                () -> new MonieFlexException("User not found")
        );
        if (userUtil.isBalanceSufficient(BigDecimal.valueOf(tvSubsDto.amount()))) {
            userUtil.updateWalletBalance(BigDecimal.valueOf(tvSubsDto.amount()), true);
            Transaction transaction = new Transaction();
            transaction.setStatus(TransactionStatus.PENDING);
            transaction.setNarration(tvSubsDto.narration());
            transaction.setAccount(tvSubsDto.card());
            transaction.setAmount(BigDecimal.valueOf(tvSubsDto.amount()));
            transaction.setReference(vtPassService.generateRequestId());
            transaction.setTransactionType(TransactionType.TV);
            transaction.setBillType(tvSubsDto.type());
            transaction.setBillVariation(tvSubsDto.code());
            transaction.setUser(user);

            var response = vtPassService.tvSubscription(tvSubsDto, transaction);
            if (response.getReference().equals(transaction.getReference())) {
                transactionRepository.save(response);
                if (response.getStatus() == TransactionStatus.FAILED) {
                    userUtil.updateWalletBalance(response.getAmount(), false);
                    return new ApiResponse<>("Transaction failed", HttpStatus.BAD_REQUEST);
                }
                return new ApiResponse<>(
                        response.getAccount(),
                        "Transaction " + response.getStatus().name().toLowerCase()
                );
            } else {
                throw new MonieFlexException("Error in completing transaction");
            }
        } else {
            throw new MonieFlexException("Insufficient balance");
        }
    }
}
