package com.sq018.monieflex.controllers;

import com.sq018.monieflex.dtos.ElectricityDto;
import com.sq018.monieflex.payloads.ApiResponse;
import com.sq018.monieflex.services.ElectricityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/bill/")
@RequiredArgsConstructor
public class UtilityBillController {
    private final ElectricityService electricityService;

    @PostMapping("/electricity")
    public ResponseEntity<ApiResponse<String>> buyElectricity(@RequestBody ElectricityDto dto) {
        var response = electricityService.buyElectricity(dto);
        return new ResponseEntity<>(response, response.getStatus());
    }
}
