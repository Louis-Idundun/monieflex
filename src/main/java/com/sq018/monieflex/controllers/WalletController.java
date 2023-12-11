package com.sq018.monieflex.controllers;

import com.sq018.monieflex.dtos.FLWVerifyAccountDto;
import com.sq018.monieflex.dtos.TransferDto;
import com.sq018.monieflex.payloads.ApiResponse;
import com.sq018.monieflex.payloads.flutterwave.AllBanksData;
import com.sq018.monieflex.services.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

@RestController
@RequestMapping("/wallet")
@RequiredArgsConstructor
public class WalletController {
    private final WalletService walletService;

    @GetMapping("/all-banks")
    public ResponseEntity<ApiResponse<List<AllBanksData>>> getAllBanks() {
        var response = walletService.getAllBanks();
        return new ResponseEntity<>(response, response.getStatus());
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verifyBankAccount(@RequestBody FLWVerifyAccountDto request) {
        var response = walletService.verifyBankAccount(request);
        return new ResponseEntity<>(response, response.getStatus());
    }

    @PostMapping("/transfer-to-bank")
    public ResponseEntity<ApiResponse<String>> transferToBank(
            @RequestBody TransferDto dto,
            @AuthenticationPrincipal UserDetails user
    ) {
        var response = walletService.transferToBank(dto);
        System.out.println(user.getUsername());
        return new ResponseEntity<>(response, response.getStatus());
    }
}
