package com.sq018.monieflex.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

public record FLWVirtualDto(
        @JsonProperty("email")
        String emailAddress,
        @JsonProperty("bvn")
        String bvn,
        @JsonProperty("firstname")
        String firstName,
        @JsonProperty("lastname")
        String lastName,
        @JsonProperty("phonenumber")
        String phoneNumber,
        @JsonProperty("amount")
        Integer amount,
        @JsonProperty("is_permanent")
        Boolean isPermanent,
        @JsonProperty("tx_ref")
        String txRef
) {
}
