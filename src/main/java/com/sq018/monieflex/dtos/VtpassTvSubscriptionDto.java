package com.sq018.monieflex.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

public record VtpassTvSubscriptionDto(
        @JsonProperty("request_id")
        String requestId,
        @JsonProperty("serviceID")
        String serviceId,
        String billersCode,
        @JsonProperty("variation_code")
        String variationCode,
        Integer amount,
        String phone,
        @JsonProperty("subscription_type")
        String subscriptionType

) { }
