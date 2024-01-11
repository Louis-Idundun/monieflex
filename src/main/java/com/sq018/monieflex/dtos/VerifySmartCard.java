package com.sq018.monieflex.dtos;

import com.sq018.monieflex.enums.BillType;
import lombok.Data;

@Data
public class VerifySmartCard {
    private String card;
    private BillType type;
}
