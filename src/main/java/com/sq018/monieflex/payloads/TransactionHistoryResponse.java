package com.sq018.monieflex.payloads;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sq018.monieflex.enums.BillType;
import com.sq018.monieflex.enums.TransactionStatus;
import com.sq018.monieflex.enums.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TransactionHistoryResponse {
        @JsonProperty("id")
        private Long id;
        @JsonProperty("amount")
        private BigDecimal amount;
        @JsonProperty("status")
        private TransactionStatus status;
        @JsonProperty("account")
        private String account;
        @JsonProperty("receiving_bank_name")
        private String receivingBankName;
        @JsonProperty("transaction_type")
        private TransactionType transactionType;
        @JsonProperty("provider_reference")
        private String providerReference;
        @JsonProperty("bill_type")
        private BillType billType;
}
