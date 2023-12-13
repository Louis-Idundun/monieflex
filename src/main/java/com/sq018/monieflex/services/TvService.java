package com.sq018.monieflex.services;

import com.sq018.monieflex.exceptions.MonieFlexException;
import com.sq018.monieflex.payloads.vtpass.VtpassTVariationResponse;
import com.sq018.monieflex.services.providers.VtPassService;
import com.sq018.monieflex.utils.VtpassEndpoints;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import com.sq018.monieflex.dtos.VtPassVerifySmartCardDto;
import com.sq018.monieflex.payloads.ApiResponse;
import com.sq018.monieflex.payloads.vtpass.TvSubscriptionQueryContent;
import com.sq018.monieflex.services.providers.VtPassService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class TvService {
    private final VtPassService vtPassService;
    private final RestTemplate restTemplate;

    public VtpassTVariationResponse getTvVariations(String code) {
        HttpEntity<Object> entity = new HttpEntity<>(vtPassService.vtPassGetHeader());
        var response = restTemplate.exchange(
                VtpassEndpoints.VARIATION_URL(code), HttpMethod.GET, entity,
                VtpassTVariationResponse.class
        );
        if(response.getStatusCode().is2xxSuccessful()) {
            return response.getBody();
        } else {
            throw new MonieFlexException("Request failed");
        }
    }

    public ApiResponse<TvSubscriptionQueryContent> queryTvAccount(VtPassVerifySmartCardDto smartCard) {
        return vtPassService.queryTVAccount(smartCard);
    }
}
