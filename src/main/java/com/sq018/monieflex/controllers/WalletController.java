package com.sq018.monieflex.controllers;

import com.sq018.monieflex.payloads.FLWAllBanksResponse;
import com.sq018.monieflex.services.providers.FlutterwaveService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/wallet")
@RequiredArgsConstructor
public class WalletController {
    private final FlutterwaveService flutterwave;

    @GetMapping("/get_banks")
    public FLWAllBanksResponse getAllBanks() {
        return flutterwave.getAllBanks();
    }
}
