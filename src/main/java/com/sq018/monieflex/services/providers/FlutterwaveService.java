package com.sq018.monieflex.services.providers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sq018.monieflex.payloads.FLWAllBanksResponse;
import com.sq018.monieflex.utils.FlutterwaveEndpoints;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@Service
public class FlutterwaveService {
    @Value("${monieFlex.flutterwave.public-key}")
    private String FLW_PUBLIC_KEY;

    @Value("${monieFlex.flutterwave.secret-key}")
    private String FLW_SECRET_KEY;

    @Value("${monieFlex.flutterwave.encryption-key}")
    private String FLW_ENC_KEY;

    private String endpoint;
    private final RestTemplate rest;

    public FlutterwaveService(RestTemplate rest) {
        this.rest = rest;
    }

    public HttpHeaders getFlutterwaveHeader(){
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer FLWSECK_TEST-624f1a1740dbf3296b5f59feefc0c476-X");
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    @SneakyThrows
    public FLWAllBanksResponse getAllBanks() {
        ObjectMapper mapper = new ObjectMapper();

        HttpEntity<Object> entity = new HttpEntity<>(getFlutterwaveHeader());
        var response = rest.exchange(
                FlutterwaveEndpoints.GET_ALL_BANKS,
                HttpMethod.GET, entity, Object.class
        );
        return mapper.readValue(mapper.writeValueAsString(response.getBody()), FLWAllBanksResponse.class);
    }
}
