package com.sq018.monieflex.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sq018.monieflex.enums.SubscriptionType;

public record TvSubsDto (
        String serviceId,
        @JsonProperty("billers_code")
        String billersCode,
        @JsonProperty("variation_code")
        String variationCode,

        Integer amount,
        String phone,
        SubscriptionType subscriptionType
)
{

}

