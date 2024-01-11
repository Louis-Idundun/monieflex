package com.sq018.monieflex.services.providers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sq018.monieflex.dtos.*;
import com.sq018.monieflex.entities.Transaction;
import com.sq018.monieflex.enums.TransactionStatus;
import com.sq018.monieflex.payloads.vtpass.*;
import com.sq018.monieflex.exceptions.MonieFlexException;
import com.sq018.monieflex.payloads.ApiResponse;
import com.sq018.monieflex.payloads.vtpass.VtpassDataSubscriptionResponse;
import com.sq018.monieflex.payloads.vtpass.VtpassDataVariation;
import com.sq018.monieflex.payloads.vtpass.VtpassDataVariationResponse;
import com.sq018.monieflex.payloads.vtpass.VtPassElectricityResponse;
import com.sq018.monieflex.payloads.vtpass.VtpassTVariation;
import com.sq018.monieflex.payloads.vtpass.VtpassTVariationResponse;
import com.sq018.monieflex.utils.VtpassEndpoints;
import lombok.SneakyThrows;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import com.sq018.monieflex.dtos.AirtimeDto;
import com.sq018.monieflex.dtos.VtPassAirtimeDto;
import com.sq018.monieflex.payloads.vtpass.VtPassAirtimeResponse;
import com.sq018.monieflex.dtos.VtPassVerifySmartCardDto;
import com.sq018.monieflex.payloads.vtpass.TvSubscriptionQueryContent;
import com.sq018.monieflex.payloads.vtpass.VtPassTvSubscriptionQueryResponse;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
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

    public String generateRequestId() {
        StringBuilder result = new StringBuilder();
        ZoneId gmtPlus1Zone = ZoneId.of("GMT+1");
        LocalDateTime gmtPlus1DateTime = LocalDateTime.now(gmtPlus1Zone);
        String date = gmtPlus1DateTime.format(DateTimeFormatter.ISO_DATE);
        result.append(date.replaceAll("-", ""));
        result.append(String.format("%02d", gmtPlus1DateTime.getHour()));
        result.append(String.format("%02d", gmtPlus1DateTime.getMinute()));
        result.append(UUID.randomUUID().toString(), 0, 15);
        return result.toString();
    }

    public HttpHeaders vtPassPostHeader() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("api-key", API_KEY);
        headers.set("secret-key", SECRET_KEY);
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    public HttpHeaders vtPassGetHeader(){
        HttpHeaders headers = new HttpHeaders();
        headers.set("api_key", API_KEY);
        headers.set("secret_key", PUBLIC_KEY);
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }


    @SneakyThrows
    public ApiResponse<TvSubscriptionQueryContent> queryTVAccount(VerifySmartCard body) {
        VtPassVerifySmartCardDto dto = new VtPassVerifySmartCardDto(body.getCard(), body.getType().getType());
        HttpEntity<VtPassVerifySmartCardDto> verifyBody = new HttpEntity<>(dto, vtPassPostHeader());
        var response = restTemplate.postForObject(
                VtpassEndpoints.VERIFY_NUMBER,
                verifyBody, VtPassTvSubscriptionQueryResponse.class
        );
        if(Objects.requireNonNull(response).getStatusCode().equalsIgnoreCase("000")) {
            if(ObjectUtils.isNotEmpty(response.getContent())) {
                ObjectMapper mapper = new ObjectMapper();
                var content = mapper.readValue(
                        mapper.writeValueAsString(response.getContent()),
                        TvSubscriptionQueryContent.class
                );
                return new ApiResponse<>(
                        content,
                        "Account found"
                );
            }
        } else if(Objects.requireNonNull(response).getContent() instanceof String) {
            throw new MonieFlexException(response.getContent().toString());
        }
        throw new MonieFlexException("Request failed");
    }

    public ApiResponse<List<VtpassDataVariation>> getDataVariations(String code) {
        HttpEntity<Object> entity = new HttpEntity<>(vtPassGetHeader());
        var response = restTemplate.exchange(
                VtpassEndpoints.VARIATION_URL(code), HttpMethod.GET, entity, VtpassDataVariationResponse.class
        );
        if(response.getStatusCode().is2xxSuccessful()){
            if(Objects.requireNonNull(response.getBody()).getDescription() != null) {
                if (response.getBody().getDescription().equalsIgnoreCase("000")) {
                    if (ObjectUtils.isNotEmpty(response.getBody().getContent().getVariations())) {
                        return new ApiResponse<>(
                                response.getBody().getContent().getVariations(),
                                "Request successfully processed");
                    }
                }
            }
        }
        throw new MonieFlexException("Request failed");
    }

    public ApiResponse<List<VtpassTVariation>> getTvVariations(String code) {
        HttpEntity<Object> entity = new HttpEntity<>(vtPassGetHeader());
        var response = restTemplate.exchange(
                VtpassEndpoints.VARIATION_URL(code), HttpMethod.GET, entity,
                VtpassTVariationResponse.class
        );
        if(response.getStatusCode().is2xxSuccessful()) {
            if(Objects.requireNonNull(response.getBody()).getDescription().equalsIgnoreCase("000")) {
                if(ObjectUtils.isNotEmpty(response.getBody().getContent().getVariations())) {
                    return new ApiResponse<>(
                            response.getBody().getContent().getVariations(),
                            "Request Successfully processed"
                    );
                }
            }
        }
        throw new MonieFlexException("Request failed");
    }

    @SneakyThrows
    public Transaction electricitySubscription(ElectricityDto electricityDto, Transaction transaction) {
        VtPassElectricityDto vtElectricity = new VtPassElectricityDto(
                transaction.getReference(),
                electricityDto.type().getType(),
                electricityDto.meterNumber(),
                electricityDto.productType().getType(),
                electricityDto.amount(),
                electricityDto.phone(),
                electricityDto.narration()

        );
        HttpEntity<VtPassElectricityDto> buyBody = new HttpEntity<>(vtElectricity, vtPassPostHeader());
        var buyResponse = restTemplate.postForEntity(
                VtpassEndpoints.PAY,
                buyBody, VtPassElectricityResponse.class);
        if(Objects.requireNonNull(buyResponse.getBody()).getResponseDescription().toLowerCase().contains("success")) {
            var reference = buyResponse.getBody().getToken() != null
                    ? buyResponse.getBody().getToken()
                    : buyResponse.getBody().getExchangeReference();
            transaction.setNarration(electricityDto.narration());
            transaction.setReference(buyResponse.getBody().getRequestId());
            transaction.setProviderReference(reference);
            transaction.setStatus(TransactionStatus.SUCCESSFUL);
        } else {
            transaction.setStatus(TransactionStatus.FAILED);
        }
        return transaction;
    }


    @SneakyThrows
    public Transaction dataSubscription(DataSubscriptionDto dataSubscriptionDto, Transaction transaction) {
        VtpassDataSubscriptionDto vtData = new VtpassDataSubscriptionDto(
                transaction.getReference(),
                dataSubscriptionDto.type().getType(),
                dataSubscriptionDto.phone(),
                dataSubscriptionDto.data(),
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
            transaction.setReference(buyResponse.getBody().getRequestId());
            transaction.setProviderReference(reference);
            transaction.setStatus(TransactionStatus.SUCCESSFUL);
        } else {
            transaction.setStatus(TransactionStatus.FAILED);
        }
        return transaction;
    }

    public Transaction buyAirtime(AirtimeDto airtime, Transaction transaction) {
        VtPassAirtimeDto airtimeDto = new VtPassAirtimeDto(
                generateRequestId(),
                airtime.network().getType(),
                airtime.amount(),
                airtime.phoneNumber()
        );
        HttpEntity<VtPassAirtimeDto> entity = new HttpEntity<>(airtimeDto, vtPassPostHeader());
        System.out.println(entity.getHeaders());
        var response = restTemplate.postForEntity(
                VtpassEndpoints.PAY, entity, VtPassAirtimeResponse.class
        );
        System.out.println("::::::: " + response);
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

    @SneakyThrows
    public ApiResponse<VtPassVerifyMeterContent> queryElectricityAccount(VerifyMeterDto verifyMeter) {
        VtPassVerifyMeterDto data = new VtPassVerifyMeterDto(
                verifyMeter.product().getType(),
                verifyMeter.meter(),
                verifyMeter.disco().getType()
        );
        System.out.println(data);
        HttpEntity<VtPassVerifyMeterDto> verifyBody = new HttpEntity<>(data, vtPassPostHeader());
        var response = restTemplate.postForObject(
                VtpassEndpoints.VERIFY_NUMBER,
                verifyBody, VtPassVerifyMeterResponse.class
        );
        System.out.println(response);
        if(Objects.requireNonNull(response).getCode().equalsIgnoreCase("000")) {
            System.out.println(response.getContent() );
            if(ObjectUtils.isNotEmpty(response.getContent().getCustomerName())) {
                return new ApiResponse<>(
                        response.getContent(),
                        "Account verified"
                );
            }
        }
        throw new MonieFlexException("Error: Couldn't fetch account");
    }

    @SneakyThrows
    public Transaction tvSubscription(TvSubsDto tvSubsDto, Transaction transaction ){
        VtpassTvSubscriptionDto vtpassTv = new VtpassTvSubscriptionDto(
                transaction.getReference(),
                tvSubsDto.type().getType(),
                tvSubsDto.card(),
                tvSubsDto.code(),
                tvSubsDto.amount(),
                tvSubsDto.phone(),
                tvSubsDto.subscriptionType().getType()
        );
        HttpEntity<VtpassTvSubscriptionDto> buyBody = new HttpEntity<>(vtpassTv,vtPassPostHeader());
        var response = restTemplate.postForEntity(VtpassEndpoints.PAY, buyBody, VtPassTvSubscriptionResponse.class);

        if(Objects.requireNonNull(response.getBody()).getCode().toLowerCase().contains("000")){
            if(response.getBody().getContent() != null) {
                transaction.setProviderReference(response.getBody().getRequestId());
                transaction.setStatus(TransactionStatus.SUCCESSFUL);
            }
        }else {
            transaction.setStatus(TransactionStatus.FAILED);
        }
        return transaction;
    }
}