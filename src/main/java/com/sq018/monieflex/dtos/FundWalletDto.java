package com.sq018.monieflex.dtos;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.validator.constraints.CreditCardNumber;

import java.math.BigDecimal;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class FundWalletDto {
    @CreditCardNumber(message = "Card number must be correct")
    private String cardNumber;
    @Pattern(regexp = "^(0[1-9]|1[0-2])\\/?([0-9]{4}|[0-9]{2})$")
    private String expiryDate;
    @Size(min = 3, max = 4, message = "CVV cannot be less than 3 or more than 4")
    private String cvv;
    @DecimalMin(value = "100.0", inclusive = true, message = "Amount to fund must be at least 100.0")
    private BigDecimal amount;
    private String cardName;
}
