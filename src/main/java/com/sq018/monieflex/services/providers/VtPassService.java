package com.sq018.monieflex.services.providers;

import com.sq018.monieflex.dtos.AirtimeDto;
import com.sq018.monieflex.dtos.VtPassAirtimeDto;
import com.sq018.monieflex.exceptions.MonieFlexException;
import com.sq018.monieflex.payloads.vtpass.VtPassAirtimeResponse;
import com.sq018.monieflex.utils.VtpassEndpoints;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.UUID;

@Service
public class VtPassService {
    @Value("${VTPASS_PUBLIC_KEY}")
    private String PUBLIC_KEY;
    @Value("${VTPASS_SECRET_KEY}")
    private String SECRET_KEY;
    @Value("${VTPASS_API_KEY}")
    private String API_KEY;

    private final RestTemplate restTemplate;

    public VtPassService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public HttpHeaders vtPassPostHeader() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("api-key", API_KEY);
        headers.add("secret-key", SECRET_KEY);
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    public HttpHeaders vtPassGetHeader() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("api-key", API_KEY);
        headers.add("public-key", PUBLIC_KEY);
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    protected String generateRequestId() {
        StringBuilder result = new StringBuilder();
        String date = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE);
        result.append(date.replaceAll("-", ""));
        result.append(LocalDateTime.now().getHour());
        result.append(LocalDateTime.now().getMinute());
        result.append(UUID.randomUUID().toString().substring(0, 15));
        return result.toString();
    }

    public VtPassAirtimeResponse buyAirtime(AirtimeDto airtime) {
        VtPassAirtimeDto airtimeDto = new VtPassAirtimeDto(
                generateRequestId(),
                airtime.network(),
                airtime.amount(),
                airtime.phoneNumber()
        );
        HttpEntity<VtPassAirtimeDto> entity = new HttpEntity<>(airtimeDto, vtPassPostHeader());
        System.out.println(entity.getHeaders());
        var response = restTemplate.postForEntity(
                VtpassEndpoints.BUY_AIRTIME, entity, VtPassAirtimeResponse.class
        );
        return response.getBody();
//        System.out.println(response.getBody().responseDescription);
//        if(response.getStatusCode().is2xxSuccessful()) {
//            var data = response.getBody();
//            if(Objects.requireNonNull(data).responseDescription.toLowerCase().contains("success")) {
//                return data;
//            }
//        }
//        throw new MonieFlexException("You cannot reap where you did not sow! OLE!!!!");
    }
}