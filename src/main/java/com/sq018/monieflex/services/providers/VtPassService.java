package com.sq018.monieflex.services.providers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sq018.monieflex.dtos.ElectricityDto;
import com.sq018.monieflex.dtos.VtPassElectricityDto;
import com.sq018.monieflex.dtos.VtPassVerifyMeterDto;
import com.sq018.monieflex.entities.transactions.Transaction;
import com.sq018.monieflex.enums.TransactionStatus;
import com.sq018.monieflex.exceptions.PaymentException;
import com.sq018.monieflex.payloads.vtpass.VtPassElectricityResponse;
import com.sq018.monieflex.payloads.vtpass.VtPassErrorResponse;
import com.sq018.monieflex.payloads.vtpass.VtPassVerifyMeterResponse;
import com.sq018.monieflex.utils.VtpassEndpoints;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
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
        headers.set("api-key", API_KEY);
        headers.set("secret-key", SECRET_KEY);
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    public HttpHeaders vtPassGetHeader() {
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
        result.append(UUID.randomUUID());
        return result.toString();
    }

    @SneakyThrows
    public Transaction electricitySubscription(ElectricityDto electricityDto, Transaction transaction) {
        VtPassElectricityDto vtElectricty = new VtPassElectricityDto(
                transaction.getReference(),
                electricityDto.serviceID(),
                electricityDto.billersCode(),
                electricityDto.variationCode().getType(),
                electricityDto.amount(),
                electricityDto.phone()
        );
        VtPassVerifyMeterDto verifyMeter = new VtPassVerifyMeterDto(
                vtElectricty.variationCode(),
                vtElectricty.serviceID(),
                vtElectricty.billersCode()
        );
        HttpEntity<VtPassVerifyMeterDto> verifyBody = new HttpEntity<>(verifyMeter, vtPassPostHeader());
        var verifyResponse = restTemplate.postForObject(
                VtpassEndpoints.VERIFY_NUMBER,
                verifyBody, Object.class
        );
        ObjectMapper mapper = new ObjectMapper();
        try {
            mapper.readValue(mapper.writeValueAsString(verifyResponse), VtPassVerifyMeterResponse.class);
            HttpEntity<VtPassElectricityDto> buyBody = new HttpEntity<>(vtElectricty, vtPassPostHeader());
            var buyResponse = restTemplate.postForEntity(
                    VtpassEndpoints.PAY,
                    buyBody, VtPassElectricityResponse.class
            );
            if(Objects.requireNonNull(buyResponse.getBody()).getResponseDescription().toLowerCase().contains("success")) {
                var reference = buyResponse.getBody().getToken() != null
                        ? buyResponse.getBody().getToken()
                        : buyResponse.getBody().getExchangeReference();
                transaction.setNarration("Electricity Billing");
                transaction.setReference(buyResponse.getBody().getRequestId());
                transaction.setProviderReference(reference);
                transaction.setStatus(TransactionStatus.SUCCESSFUL);
                return transaction;
            } else {
                transaction.setStatus(TransactionStatus.FAILED);
                throw new PaymentException(
                        buyResponse.getBody().getResponseDescription(),
                        BigDecimal.valueOf(electricityDto.amount()),
                        transaction
                );
            }
        } catch (JsonProcessingException e) {
            var result = mapper.readValue(mapper.writeValueAsString(verifyResponse), VtPassErrorResponse.class);
            transaction.setStatus(TransactionStatus.FAILED);
            throw new PaymentException(
                    result.getResponseDescription(),
                    BigDecimal.valueOf(electricityDto.amount()),
                    transaction
            );
        }
    }
}
