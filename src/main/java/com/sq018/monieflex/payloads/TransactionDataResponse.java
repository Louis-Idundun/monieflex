package com.sq018.monieflex.payloads;

import lombok.Data;

@Data
public class TransactionDataResponse {
    private String month;
    private String income;
    private String expense;
}
