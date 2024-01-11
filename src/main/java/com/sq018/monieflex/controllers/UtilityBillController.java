package com.sq018.monieflex.controllers;


import com.sq018.monieflex.dtos.*;
import com.sq018.monieflex.enums.BillType;
import com.sq018.monieflex.payloads.ApiResponse;
import com.sq018.monieflex.payloads.vtpass.VtPassVerifyMeterContent;
import com.sq018.monieflex.payloads.vtpass.VtpassDataVariation;
import com.sq018.monieflex.services.ElectricityService;
import com.sq018.monieflex.payloads.vtpass.VtpassTVariation;
import com.sq018.monieflex.services.AirtimeService;
import com.sq018.monieflex.services.DataService;
import com.sq018.monieflex.services.TvService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


import java.util.List;

import com.sq018.monieflex.payloads.vtpass.TvSubscriptionQueryContent;


@RestController
@RequestMapping("/bill")
@RequiredArgsConstructor
public class UtilityBillController {
    private final ElectricityService electricityService;
    private final TvService tvService;
    private final DataService dataService;
    private final AirtimeService airtimeService;


    @PostMapping("/electricity")
    public ResponseEntity<ApiResponse<String>> buyElectricity(@RequestBody ElectricityDto dto) {
        var response = electricityService.buyElectricity(dto);
        return new ResponseEntity<>(response, response.getStatus());
    }

    @PostMapping("/tv-subscription")
    public ResponseEntity<ApiResponse<String>> payTvSubscription(@RequestBody TvSubsDto dto) {
        var response = tvService.payTvSubscription(dto);
        return new ResponseEntity<>(response, response.getStatus());
    }

    @GetMapping("/data-variations")
    public ResponseEntity<ApiResponse<List<VtpassDataVariation>>> fetchDataVariation(@RequestParam BillType code) {
        var response = dataService.viewDataVariations(code);
        return new ResponseEntity<>(response, response.getStatus());

    }

     @GetMapping("/tv-variations")
     public ResponseEntity<ApiResponse<List<VtpassTVariation>>> fetchTvVariation(@RequestParam BillType code) {
         var response = tvService.viewTvVariations(code);
         return new ResponseEntity<>(response, response.getStatus());

     }

    @PostMapping("/data-purchase")
    public ResponseEntity<ApiResponse<String>> buyData(@RequestBody DataSubscriptionDto dataSubscriptionDto){
        var response = dataService.buyData(dataSubscriptionDto);
        return new ResponseEntity<>(response, response.getStatus());
    }

    @PostMapping("/airtime")
    public ResponseEntity<ApiResponse<String>> airtime(@RequestBody @Validated AirtimeDto body) {
        var response = airtimeService.buyAirtime(body);
        return new ResponseEntity<>(response, response.getStatus());
    }

    @PostMapping("/verify-electricity")
    public ResponseEntity<ApiResponse<VtPassVerifyMeterContent>> queryElectricityAccount(
            @RequestBody VerifyMeterDto verifyMeter) {
        var response = electricityService.queryElectricityAccount(verifyMeter);
        return new ResponseEntity<>(response, response.getStatus());
    }

    @PostMapping("/verify-smart-card")
    public ResponseEntity<ApiResponse<TvSubscriptionQueryContent>> queryTvAccount(
            @RequestBody VerifySmartCard smartCard
    ) {
        var response = tvService.queryTvAccount(smartCard);
        return new ResponseEntity<>(response, response.getStatus());
    }
}
