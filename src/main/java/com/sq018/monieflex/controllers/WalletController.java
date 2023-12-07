package com.sq018.monieflex.controllers;

import com.sq018.monieflex.payloads.ApiResponse;
import com.sq018.monieflex.payloads.flwallbankresponse.AllBanksData;
import com.sq018.monieflex.payloads.flwallbankresponse.FLWAllBanksResponse;
import com.sq018.monieflex.services.WalletService;
import com.sq018.monieflex.services.providers.FlutterwaveService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
