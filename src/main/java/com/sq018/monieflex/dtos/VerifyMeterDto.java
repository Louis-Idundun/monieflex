package com.sq018.monieflex.dtos;

import com.sq018.monieflex.enums.BillType;
import com.sq018.monieflex.enums.ElectricityType;

public record VerifyMeterDto(
        ElectricityType product,
        BillType disco,
        String meter
) {
}
