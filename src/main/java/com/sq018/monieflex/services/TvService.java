package com.sq018.monieflex.services;


import com.sq018.monieflex.exceptions.MonieFlexException;
import com.sq018.monieflex.payloads.vtpass.VtpassTVariationResponse;
import com.sq018.monieflex.payloads.ApiResponse;
import com.sq018.monieflex.payloads.vtpass.VtpassTVariation;
import com.sq018.monieflex.services.providers.VtPassService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class TvService {
    private final VtPassService vtPassService;

        public ApiResponse<List<VtpassTVariation>> viewTvVariations (String code){
            return vtPassService.getTvVariations(code);
        }
    }

