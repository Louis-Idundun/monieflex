package com.sq018.monieflex.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum NetworkType {
    MTN("Mtn"),
    GLO("Glo"),
    AIRTEL("Airtel"),
    NINE_MOBILE("9Mobile");

    private final String type;
}
