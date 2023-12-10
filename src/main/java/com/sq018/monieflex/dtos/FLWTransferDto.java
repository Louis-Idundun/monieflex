package com.sq018.monieflex.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

public record FLWTransferDto(
        @JsonProperty("account_bank")
        String accountBank,
        @JsonProperty("account_number")
        String accountNumber,
        @JsonProperty("amount")
        Integer amount,
        @JsonProperty("narration")
        String narration,
        @JsonProperty("currency")
        String currency,
        @JsonProperty("reference")
        String reference
) {
}
