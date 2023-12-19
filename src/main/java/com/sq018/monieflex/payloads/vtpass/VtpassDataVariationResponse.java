package com.sq018.monieflex.payloads.vtpass;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class VtpassDataVariationResponse {
    @JsonProperty("response_description")
    private String description;
    @JsonProperty("content")
    private VtpassDataVariationContent content;
}
