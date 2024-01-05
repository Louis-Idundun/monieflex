package com.sq018.monieflex.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class LocalTransferRequest {

    @NotBlank(message = "Account number cannot be empty")
    private String accountNumber;

    @Min(value = 1, message = "Amount should not be less than 1")
    @NotBlank(message = "Amount cannot be empty")
    private BigDecimal amount;

    private String receiverName;

    private String narration;
}
