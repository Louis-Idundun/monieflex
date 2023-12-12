package com.sq018.monieflex.services;

import com.sq018.monieflex.exceptions.MonieFlexException;
import com.sq018.monieflex.payloads.vtpass.VtpassDataVariationResponse;
import com.sq018.monieflex.services.providers.VtPassService;
import com.sq018.monieflex.utils.VtpassEndpoints;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class DataService {

    private final RestTemplate restTemplate;
    private final VtPassService vtPassService;


    public VtpassDataVariationResponse getDataVariations(String code){
        HttpEntity<Object> entity = new HttpEntity<>(vtPassService.vtPassGetHeader());
        var response = restTemplate.exchange(
                VtpassEndpoints.VARIATION_URL(code), HttpMethod.GET, entity, VtpassDataVariationResponse.class
        );
        if(response.getStatusCode().is2xxSuccessful()){
            return response.getBody();
        } else {
            throw new MonieFlexException("Request failed");
        }
    }

}
