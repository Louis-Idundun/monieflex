package com.sq018.monieflex.services;


import com.sq018.monieflex.dtos.VtPassVerifySmartCardDto;
import com.sq018.monieflex.payloads.vtpass.TvSubscriptionQueryContent;
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
    public ApiResponse<TvSubscriptionQueryContent> queryTvAccount(VtPassVerifySmartCardDto smartCard) {
        return vtPassService.queryTVAccount(smartCard);
        }
    }

