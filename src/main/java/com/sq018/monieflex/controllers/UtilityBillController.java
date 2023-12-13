package com.sq018.monieflex.controllers;

import com.sq018.monieflex.dtos.AirtimeDto;
import com.sq018.monieflex.payloads.ApiResponse;
import com.sq018.monieflex.services.AirtimeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UtilityBillController {
    private final AirtimeService airtimeService;

    @PostMapping("/airtime")
    public ResponseEntity<ApiResponse<String>> airtime(@RequestBody AirtimeDto body) {
        var response = airtimeService.buyAirtime(body);
        return new ResponseEntity<>(response, response.getStatus());
    }
}
