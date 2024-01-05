package com.sq018.monieflex.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ElectricityType {
    POST_PAID("postpaid"),
    PRE_PAID("prepaid");

    private final String type;
}
