package com.sq018.monieflex.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sq018.monieflex.enums.BillType;

public record DataSubscriptionDto(
        BillType type,
        String data,
        Integer amount,
        String phone,
        String narration

){

}
