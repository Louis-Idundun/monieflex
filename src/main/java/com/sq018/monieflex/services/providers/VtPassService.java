package com.sq018.monieflex.services.providers;

import com.sq018.monieflex.exceptions.MonieFlexException;
import com.sq018.monieflex.payloads.vtpass.VtpassDataVariationResponse;
import com.sq018.monieflex.utils.VtpassEndpoints;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class VtPassService {

    @Value("${VT_PUBLIC_KEY}")
    private String PUBLIC_KEY;
    @Value("${VT_SECRET_KEY}")
    private String SECRET_KEY;
    @Value("${VT_API_KEY}")
    private String API_KEY;

    private final RestTemplate restTemplate;

    public VtPassService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public HttpHeaders vtPassPostHeader(){
        HttpHeaders headers = new HttpHeaders();
        headers.set("api-key", API_KEY);
        headers.set("secret-key", SECRET_KEY);
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    public HttpHeaders vtPassGetHeader(){
        HttpHeaders headers = new HttpHeaders();
        headers.set("api-key", API_KEY);
        headers.set("public-key", PUBLIC_KEY);
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    public VtpassDataVariationResponse getDataVariations(String code){
        HttpEntity<Object> entity = new HttpEntity<>(vtPassGetHeader());
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
