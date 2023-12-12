package com.sq018.monieflex.services.providers;


import com.sq018.monieflex.dtos.DataSubscriptionDto;
import com.sq018.monieflex.dtos.VtpassDataSubscriptionDto;
import com.sq018.monieflex.entities.transactions.Transaction;
import com.sq018.monieflex.enums.TransactionStatus;
import com.sq018.monieflex.payloads.vtpass.VtpassDataSubscriptionResponse;
import com.sq018.monieflex.utils.VtpassEndpoints;
import lombok.SneakyThrows;
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


    public String generateRequestId() {
        StringBuilder result = new StringBuilder();
        String date = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE);
        result.append(date.replaceAll("-", ""));
        result.append(LocalDateTime.now().getHour());
        result.append(LocalDateTime.now().getMinute());
        result.append(UUID.randomUUID().toString().substring(0, 15));
        return result.toString();
    }

    @SneakyThrows
    public Transaction dataSubscription(DataSubscriptionDto dataSubscriptionDto, Transaction transaction) {
        VtpassDataSubscriptionDto vtData = new VtpassDataSubscriptionDto(
                transaction.getReference(),
                dataSubscriptionDto.serviceID(),
                dataSubscriptionDto.billersCode(),
                dataSubscriptionDto.variationCode(),
                dataSubscriptionDto.amount(),
                dataSubscriptionDto.phone()
        );
        HttpEntity<VtpassDataSubscriptionDto> buyBody = new HttpEntity<>(vtData, vtPassPostHeader());
        var buyResponse = restTemplate.postForEntity(
                VtpassEndpoints.PAY,
                buyBody, VtpassDataSubscriptionResponse.class
        );
        if(Objects.requireNonNull(buyResponse.getBody()).getResponseDescription().toLowerCase().contains("success")) {
            var reference = buyResponse.getBody().getToken() != null
                    ? buyResponse.getBody().getToken()
                    : buyResponse.getBody().getExchangeReference();
            transaction.setNarration("Data Billing");
            transaction.setReference(buyResponse.getBody().getRequestId());
            transaction.setProviderReference(reference);
            transaction.setStatus(TransactionStatus.SUCCESSFUL);
        } else {
            transaction.setStatus(TransactionStatus.FAILED);
        }
        return transaction;
    }
}
