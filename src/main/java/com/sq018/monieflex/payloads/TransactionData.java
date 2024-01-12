package com.sq018.monieflex.payloads;

import lombok.Data;


@Data
public class TransactionData {
    private String month;
    private String income;
    private String expense;
}
