package com.sq018.monieflex.controllers;

import com.sq018.monieflex.dtos.*;
import com.sq018.monieflex.enums.TransactionType;
import com.sq018.monieflex.payloads.*;
import com.sq018.monieflex.payloads.flutterwave.AllBanksData;
import com.sq018.monieflex.services.WalletService;
import jakarta.validation.Valid;
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

    @GetMapping("/view-transactions")
    public ResponseEntity<ApiResponse<TransactionHistoryResponse>> viewTransactions(
            @RequestParam Integer page, @RequestParam Integer size
    ) {
        var response = walletService.queryHistory(page, size);
        return new ResponseEntity<>(response, response.getStatus());
    }

    @GetMapping("/history")
    public ResponseEntity<ApiResponse<List<TransactionHistory>>> viewHistory(
            @RequestParam Integer page, @RequestParam Integer size, @RequestParam TransactionType type
            ) {
        var response = walletService.queryHistory(page, size, type);
        return new ResponseEntity<>(response, response.getStatus());
    }

    @GetMapping("/details")
    public ResponseEntity<ApiResponse<WalletPayload>> fetchWalletDetails() {
        var response = walletService.queryWalletDetails();
        return new ResponseEntity<>(response, response.getStatus());
    }

    @PostMapping("/local/transfer")
    public ResponseEntity<ApiResponse<?>> localTransfer(@RequestBody LocalTransferRequest localTransferRequest){
        ApiResponse<?> response = walletService.localTransfer(localTransferRequest);
        return new ResponseEntity<>(response, response.getStatus());
    }

    @PostMapping("/verify/local/account")
    public ResponseEntity<ApiResponse<?>> localAccountQuery(@RequestBody LocalAccountQueryRequest localAccountQueryRequest){
        ApiResponse<?> response = walletService.queryLocalAccount(localAccountQueryRequest);
        return new ResponseEntity<>(response, response.getStatus());
    }

    @PostMapping("/transaction-pin")
    public ResponseEntity<ApiResponse<String>> createPin(@RequestParam String pin) {
        var response = walletService.createTransactionPin(pin);
        return new ResponseEntity<>(response, response.getStatus());
    }

    @PostMapping("/verify-pin")
    public ResponseEntity<ApiResponse<String>> verifyPin(@RequestParam String pin) {
        var response = walletService.verifyPin(pin);
        return new ResponseEntity<>(response, response.getStatus());
    }
    @PostMapping("/fund-wallet")
    public ResponseEntity<ApiResponse<String>> fundWallet(@Valid @RequestBody FundWalletDto fundWalletDto) {
        var response = walletService.fundWallet(fundWalletDto);
        return new ResponseEntity<>(response, response.getStatus());
    }

    @GetMapping("/verify-otp")
    public ResponseEntity<ApiResponse<String>> verifyFundWallet(@RequestParam String otp) {
        var response = walletService.verifyFundWallet(otp);
        return new ResponseEntity<>(response, response.getStatus());
    }

    @GetMapping("/data")
    public ResponseEntity<ApiResponse<TransactionDataResponse>> viewDataChart() {
        var response = walletService.getTransactionChart();
        return new ResponseEntity<>(response, response.getStatus());
    }
}
