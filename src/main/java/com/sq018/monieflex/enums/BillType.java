package com.sq018.monieflex.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BillType {
    DSTV("dstv"),
    GOTV("gotv"),
    STARTIMES("startimes"),
    SHOWMAX("showmax"),

    MTN_AIRTIME("mtn"),
    GLO_AIRTIME("glo"),
    AIRTEL_AIRTIME("airtel"),
    NINE_MOBILE_AIRTIME("etisalat"),

    MTN_DATA("mtn-data"),
    GLO_DATA("glo-data"),
    AIRTEL_DATA("airtel-data"),
    NINE_MOBILE_DATA("etisalat-data"),

    IKEDC("ikeja-electric"),
    EKEDC("eko-electric"),
    KEDCO("kano-electric"),
    PHED("portharcourt-electric"),
    JED("jos-electric"),
    IBEDC("ibadan-electric"),
    KAEDCO("kaduna-electric"),
    AEDC("abuja-electric"),
    EEDC("enugu-electric"),
    BEDC("benin-electric"),
    ABA("aba-electric");

    private final String type;
}
