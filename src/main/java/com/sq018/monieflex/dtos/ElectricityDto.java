package com.sq018.monieflex.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sq018.monieflex.enums.BillType;
import com.sq018.monieflex.enums.ElectricityType;

public record ElectricityDto(
        @JsonProperty("product_type")
        ElectricityType productType,
        @JsonProperty("meter_number")
        String meterNumber,
        BillType type,
        Integer amount,
        String phone,
        String narration
) {
}
