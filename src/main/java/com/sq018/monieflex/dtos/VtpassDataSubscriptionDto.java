package com.sq018.monieflex.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

public record VtpassDataSubscriptionDto(
        @JsonProperty("request_id")
        String requestId,
        String serviceID,
        String billersCode,
        @JsonProperty("variation_code")
        String variationCode,
        Integer amount,
        String phone
) {
}