package com.sq018.monieflex.controllers;

import com.sq018.monieflex.payloads.vtpass.VtpassDataVariationResponse;
import com.sq018.monieflex.services.DataService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UtilityBillController {

    private final DataService dataService;

    @GetMapping("/data-variations")
    public ResponseEntity<VtpassDataVariationResponse> fetchDataVariation(@RequestParam String code) {
        var response = dataService.getDataVariations(code);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
