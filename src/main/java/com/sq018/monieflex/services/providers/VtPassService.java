package com.sq018.monieflex.services.providers;

import com.sq018.monieflex.dtos.AirtimeDto;
import com.sq018.monieflex.dtos.VtPassAirtimeDto;
import com.sq018.monieflex.entities.transactions.Transaction;
import com.sq018.monieflex.enums.TransactionStatus;
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

    public String generateRequestId() {
        StringBuilder result = new StringBuilder();
        String date = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE);
        result.append(date.replaceAll("-", ""));
        result.append(LocalDateTime.now().getHour());
        result.append(LocalDateTime.now().getMinute());
        result.append(UUID.randomUUID());
        return result.toString();
    }

    public Transaction buyAirtime(AirtimeDto airtime, Transaction transaction) {
        VtPassAirtimeDto airtimeDto = new VtPassAirtimeDto(
                generateRequestId(),
                airtime.network().toLowerCase(),
                airtime.amount(),
                airtime.phoneNumber()
        );
        HttpEntity<VtPassAirtimeDto> entity = new HttpEntity<>(airtimeDto, vtPassPostHeader());
        System.out.println(entity.getHeaders());
        var response = restTemplate.postForEntity(
                VtpassEndpoints.BUY_AIRTIME, entity, VtPassAirtimeResponse.class
        );
        System.out.println(response.getBody());
        if(Objects.requireNonNull(response.getBody()).responseDescription.toLowerCase().contains("success")) {
            var data = response.getBody();
            transaction.setStatus(TransactionStatus.SUCCESSFUL);
            transaction.setProviderReference(data.getTransactionId());
            transaction.setUpdatedAt(LocalDateTime.now());
        } else {
            transaction.setStatus(TransactionStatus.FAILED);
        }
        return transaction;
    }
}