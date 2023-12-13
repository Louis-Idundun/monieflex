package com.sq018.monieflex.controllers;

import com.sq018.monieflex.dtos.DataSubscriptionDto;
import com.sq018.monieflex.payloads.ApiResponse;
import com.sq018.monieflex.payloads.vtpass.VtpassTVariation;
import com.sq018.monieflex.services.DataService;
import com.sq018.monieflex.services.TvService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class UtilityBillController {
    private final TvService tvService;
    private final DataService dataService;

     @GetMapping("/tv-variations")
     public ResponseEntity<ApiResponse<List<VtpassTVariation>>> fetchTvVariation(@RequestParam String code) {
         var response = tvService.viewTvVariations(code);
         return new ResponseEntity<>(response, response.getStatus());

     }

    @PostMapping("/data-purchase")
    public ResponseEntity<ApiResponse<String>> buyData(@RequestBody DataSubscriptionDto dataSubscriptionDto){
        var response = dataService.buyData(dataSubscriptionDto);
        return new ResponseEntity<>(response, response.getStatus());
    }
}
