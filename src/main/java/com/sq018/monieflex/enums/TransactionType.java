package com.sq018.monieflex.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Collections;
import java.util.List;

@Getter
@AllArgsConstructor
public enum TransactionType {
    LOCAL_TRANSFER(Collections.emptyList()),
    EXTERNAL_TRANSFER(Collections.emptyList()),
    DATA(List.of(
            BillType.NINE_MOBILE,
            BillType.MTN,
            BillType.GLO,
            BillType.AIRTEL
    )),
    AIRTIME(List.of(
            BillType.NINE_MOBILE,
            BillType.MTN,
            BillType.GLO,
            BillType.AIRTEL
    )),
    TV(List.of(
            BillType.GOTV,
            BillType.DSTV,
            BillType.SHOW_MAX,
            BillType.STAR_TIMES
    )),
    BILLS(List.of(
            BillType.POST_PAID,
            BillType.PRE_PAID
    ));

    private List<BillType> billTypes;
}
