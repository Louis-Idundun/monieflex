package com.sq018.monieflex.payloads.vtpass;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class VtpassDataVariationContent {

    @JsonProperty("serviceName")
    private String serviceName;
    @JsonProperty("serviceId")
    private String serviceId;
    @JsonProperty("varations")
    private List<VtpassDataVariation> variations;

}
