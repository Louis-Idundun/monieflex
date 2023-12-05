package com.sq018.monieflex.services.providers;

import com.sq018.monieflex.dtos.UserDto;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class VtPassService {

    private final RestTemplate restTemplate;

    public VtPassService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }


    public HttpHeaders getVtPassHeader(){
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer FLWSECK_TEST-624f1a1740dbf3296b5f59feefc0c476-X");
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

}
