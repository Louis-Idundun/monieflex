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
public class VtPassElectricityTransaction {
    private String status;
    @JsonProperty("product_name")
    private String productName;
    @JsonProperty("unique_element")
    private String uniqueElement;
    private String phone;
    private String name;
    private Integer amount;
    private String transactionId;
}
