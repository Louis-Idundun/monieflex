package com.sq018.monieflex.exceptions;

import com.sq018.monieflex.entities.Transaction;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class PaymentException extends RuntimeException {
    private final BigDecimal amount;
    private final Transaction transaction;
    public PaymentException(String message, BigDecimal amount, Transaction transaction) {
        super(message);
        this.amount = amount;
        this.transaction = transaction;
    }
}
