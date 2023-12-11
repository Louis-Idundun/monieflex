package com.sq018.monieflex.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BillType {
    POST_PAID("postpaid"),
    PRE_PAID("prepaid");

    private final String type;
}
