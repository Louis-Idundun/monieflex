package com.sq018.monieflex.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BillType {
    POST_PAID("postpaid"),
    PRE_PAID("prepaid"),
    DSTV("dstv"),
    GOTV("gotv"),
    STAR_TIMES("startimes"),
    SHOW_MAX("showmax"),
    MTN("mtn"),
    GLO("glo"),
    AIRTEL("airtel"),
    NINE_MOBILE("etisalat");

    private final String type;
}
