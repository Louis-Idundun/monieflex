package com.sq018.monieflex.controllers;

import com.sq018.monieflex.dtos.DataSubscriptionDto;
import com.sq018.monieflex.payloads.ApiResponse;
import com.sq018.monieflex.payloads.vtpass.VtpassTVariationResponse;
import com.sq018.monieflex.services.DataService;
import com.sq018.monieflex.services.TvService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Controller
public class UtilityBillController {
    private final TvService tvService;
    private final DataService dataService;

    @GetMapping("/tv-variations")
    public ResponseEntity<VtpassTVariationResponse> fetchTvVariation(@RequestParam String code) {
        var response = tvService.getTvVariations(code);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/data-purchase")
    public ResponseEntity<ApiResponse<String>> buyData(@RequestBody DataSubscriptionDto dataSubscriptionDto){
        var response = dataService.buyData(dataSubscriptionDto);
        return new ResponseEntity<>(response, response.getStatus());
    }
    private final AirtimeService airtimeService;

    @PostMapping("/airtime")
    public ResponseEntity<ApiResponse<String>> airtime(@RequestBody AirtimeDto body) {
        var response = airtimeService.buyAirtime(body);
        return new ResponseEntity<>(response, response.getStatus());
    }
}
