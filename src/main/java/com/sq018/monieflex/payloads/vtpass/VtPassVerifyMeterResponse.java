package com.sq018.monieflex.payloads.vtpass;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VtPassVerifyMeterResponse {
    @JsonProperty("code")
    private String code;
    @JsonProperty("content")
    private VtPassVerifyMeterContent content;
}
