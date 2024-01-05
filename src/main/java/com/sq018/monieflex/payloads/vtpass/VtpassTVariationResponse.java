package com.sq018.monieflex.payloads.vtpass;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class VtpassTVariationResponse {
    @JsonProperty("response_description")
    private String description;
    @JsonProperty("content")
    private VtpassTVariationContent content;
    @JsonProperty("response_Id")
    private String requestId;
    private String amount;
    @JsonProperty("purchased_code")
    private String purchasedCode;
    private String exchangeReference;
    private String token;
}
