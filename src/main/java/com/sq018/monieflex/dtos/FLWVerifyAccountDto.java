package com.sq018.monieflex.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

public record FLWVerifyAccountDto(
        @JsonProperty("account_number")
        String accountNumber,
        @JsonProperty("account_bank")
        String bankAccount
) {
}
