package com.sq018.monieflex.payloads;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WalletPayload {
    private BigDecimal balance;
    @JsonProperty("wallet_number")
    private String number;
    @JsonProperty("bank_name")
    private String bankName;
}
