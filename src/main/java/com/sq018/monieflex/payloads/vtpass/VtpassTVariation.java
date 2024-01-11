package com.sq018.monieflex.payloads.vtpass;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class VtpassTVariation {
    @JsonProperty("variation_code")
    private String code;
    @JsonProperty("name")
    private String name;
    @JsonProperty("variation_amount")
    private String amount;
}
