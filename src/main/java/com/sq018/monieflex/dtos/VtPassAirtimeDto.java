package com.sq018.monieflex.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

public record VtPassAirtimeDto(
        @JsonProperty("request_id")
        String requestId,
        @JsonProperty("serviceID")
        String serviceID,
        @JsonProperty("amount")
        Integer amount,
        @JsonProperty("phone")
        String phone
) {
}
