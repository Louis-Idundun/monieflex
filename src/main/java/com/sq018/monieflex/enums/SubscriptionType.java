package com.sq018.monieflex.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SubscriptionType {
    CHANGE("change"),
    RENEWAL("renew");

    private final String type;
}
