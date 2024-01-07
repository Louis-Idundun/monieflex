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
public class VtPassVerifyMeterContent {
    @JsonProperty("Customer_Name")
    private String customerName;
    @JsonProperty("Address")
    private String address;
}
