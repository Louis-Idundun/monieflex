package com.sq018.monieflex.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sq018.monieflex.enums.BillType;

public record ElectricityDto(
        String serviceID,
        String billersCode,
        @JsonProperty("variation_code")
        BillType variationCode,
        Integer amount,
        String phone
) {
}
