package com.sq018.monieflex.payloads.vtpass;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class VtpassTVariationContent {
    @JsonProperty("ServiceName")
    private String serviceName;
    @JsonProperty("serviceID")
    private String serviceId;
//    @JsonProperty("convinience_fee")
//    private String fee;
    @JsonProperty("varations")
    private List<VtpassTVariation> variations;
}
