package com.sq018.monieflex.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

public record VtPassElectricityDto(
        @JsonProperty("request_id")
        String requestId,
        String serviceID,
        String billersCode,
        @JsonProperty("variation_code")
        String variationCode,
        Integer amount,
        String phone,
        String narration) {
}
