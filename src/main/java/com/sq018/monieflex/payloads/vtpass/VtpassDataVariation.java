package com.sq018.monieflex.payloads.vtpass;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor
public class VtpassDataVariation {

    @JsonProperty("variation_code")
    private String code;
    @JsonProperty("name")
    private String name;
    @JsonProperty("variation_amount")
    private String amount;
}
