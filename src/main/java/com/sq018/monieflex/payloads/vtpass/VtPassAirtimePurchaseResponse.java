package com.sq018.monieflex.payloads.vtpass;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VtPassAirtimePurchaseResponse {
    @JsonProperty("request_id")
    private String requestID;
    @JsonProperty("serviceID")
    private String serviceID;
    private Integer amount;
    private String phone;
}
