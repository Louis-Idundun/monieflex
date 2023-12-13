package com.sq018.monieflex.services;


import com.sq018.monieflex.payloads.vtpass.VtpassTVariationResponse;
import com.sq018.monieflex.services.providers.VtPassService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class TvService {

    private final VtPassService vtPassService;

   public VtpassTVariationResponse viewTvVariations(String code){
       VtpassTVariationResponse response = vtPassService.getTvVariations(code);
       return response;
   }
}
