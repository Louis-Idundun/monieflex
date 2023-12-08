package com.sq018.monieflex.services;

import com.sq018.monieflex.dtos.FLWVerifyAccountDto;
import com.sq018.monieflex.entities.account.User;
import com.sq018.monieflex.entities.account.Wallet;
import com.sq018.monieflex.exceptions.MonieFlexException;
import com.sq018.monieflex.payloads.ApiResponse;
import com.sq018.monieflex.payloads.flutterwave.FLWVirtualAccountResponse;
import com.sq018.monieflex.payloads.flutterwave.VirtualAccountResponse;
import com.sq018.monieflex.payloads.flwallbankresponse.AllBanksData;
import com.sq018.monieflex.services.providers.FlutterwaveService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WalletService {

    private final FlutterwaveService flutterwaveService;

    protected String generateTxRef() {
        return "MONF-" + UUID.randomUUID().toString().substring(0, 6);
    }

    public Wallet create(User user) {
        String reference = generateTxRef();
        Wallet wallet = flutterwaveService.createWallet(
                user.getEmailAddress(), user.getBvn(), reference,
                user.getLastName(), user.getFirstName(), user.getPhoneNumber()
        );
        wallet.setUser(user);
        return wallet;
    }

    public ApiResponse<List<AllBanksData>> getAllBanks(){
        return flutterwaveService.getAllBanks();
    }

    public ApiResponse<VirtualAccountResponse> verifyBankAccount(FLWVerifyAccountDto accountDto) {
        return flutterwaveService.verifyBankAccount(accountDto);
    }
}
