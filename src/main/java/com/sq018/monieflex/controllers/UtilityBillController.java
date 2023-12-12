package com.sq018.monieflex.controllers;


import com.sq018.monieflex.dtos.ElectricityDto;
import com.sq018.monieflex.payloads.ApiResponse;
import com.sq018.monieflex.services.ElectricityService;
import com.sq018.monieflex.dtos.DataSubscriptionDto;
import com.sq018.monieflex.payloads.vtpass.VtpassTVariationResponse;
import com.sq018.monieflex.services.DataService;
import com.sq018.monieflex.services.TvService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/bill/")
@RequiredArgsConstructor
public class UtilityBillController {
    private final ElectricityService electricityService;
    private final TvService tvService;
    private final DataService dataService;

    @PostMapping("/electricity")
    public ResponseEntity<ApiResponse<String>> buyElectricity(@RequestBody ElectricityDto dto) {
        var response = electricityService.buyElectricity(dto);
        return new ResponseEntity<>(response, response.getStatus());
    }

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
}
