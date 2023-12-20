package com.sq018.monieflex.payloads.flutterwave;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class TransferResponse {
    @JsonProperty("id")
    private Integer id;
    @JsonProperty("account_number")
    private String accountNumber;
    @JsonProperty("bank_code")
    private String bankCode;
    @JsonProperty("full_name")
    private String fullName;
    @JsonProperty("created_at")
    private Date createdAt;
    @JsonProperty("currency")
    private String currency;
    @JsonProperty("debit_currency")
    private String debitCurrency;
    @JsonProperty("amount")
    private Integer amount;
    @JsonProperty("fee")
    private Integer fee;
    @JsonProperty("status")
    private String status;
    @JsonProperty("reference")
    private String reference;
    @JsonProperty("meta")
    private Object meta;
    @JsonProperty("narration")
    private String narration;
    @JsonProperty("complete_message")
    private String completeMessage;
    @JsonProperty("requires_approval")
    private Integer requiresApproval;
    @JsonProperty("is_approved")
    private Integer isApproved;
    @JsonProperty("bank_name")
    private String bankName;
}
