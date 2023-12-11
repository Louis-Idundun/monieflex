package com.sq018.monieflex.controllers;

import com.sq018.monieflex.dtos.AirtimeDto;
import com.sq018.monieflex.payloads.vtpass.VtPassAirtimeResponse;
import com.sq018.monieflex.services.providers.VtPassService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UtilityBillController {
    private final VtPassService vtPassService;

    @PostMapping("/find-thief")
    public ResponseEntity<VtPassAirtimeResponse> findThief(@RequestBody AirtimeDto body) {
        var response = vtPassService.buyAirtime(body);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
