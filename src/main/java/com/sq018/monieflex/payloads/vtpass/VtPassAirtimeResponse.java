package com.sq018.monieflex.payloads.vtpass;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VtPassAirtimeResponse {
    @JsonProperty("code")
    public String code;
    @JsonProperty("response_description")
    public String responseDescription;
    @JsonProperty("requestId")
    public String requestId;
    @JsonProperty("transactionId")
    public String transactionId;
    @JsonProperty("amount")
    public String amount;
    @JsonProperty("purchased_code")
    public String purchasedCode;
}
