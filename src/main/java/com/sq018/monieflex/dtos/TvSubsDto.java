package com.sq018.monieflex.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sq018.monieflex.enums.BillType;
import com.sq018.monieflex.enums.SubscriptionType;

public record TvSubsDto (
        BillType type,
        String card,
        String code,
        String narration,
        Integer amount,
        String phone,
        @JsonProperty("sub_type")
        SubscriptionType subscriptionType
) { }

