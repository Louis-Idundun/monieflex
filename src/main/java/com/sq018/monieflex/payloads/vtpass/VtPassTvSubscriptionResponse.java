package com.sq018.monieflex.payloads.vtpass;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class VtPassTvSubscriptionResponse {
    private String code;
    @JsonProperty("response_description")
    private String responseDescription;
    private String requestId;
    private String amount;
    @JsonProperty("purchased_code")
    private String purchasedCode;
    private VtPassTvSubContent content;
}
