package com.sq018.monieflex.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

public record DataSubscriptionDto (
        String serviceID,
        String billersCode,
        @JsonProperty("variation_code")
        String variationCode,
        Integer amount,
        String phone

){

}
