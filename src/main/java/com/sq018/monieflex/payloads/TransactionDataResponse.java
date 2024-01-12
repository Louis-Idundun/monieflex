package com.sq018.monieflex.payloads;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class TransactionDataResponse {
    private BigDecimal totalIncome;
    private BigDecimal totalExpense;
    private List<TransactionData> dataList;
}
