package com.sq018.monieflex.payloads;

import lombok.Data;

import java.util.List;

@Data
public class TransactionHistoryResponse {
    private Integer pages;
    private Long elements;
    private List<TransactionHistory> data;
}
