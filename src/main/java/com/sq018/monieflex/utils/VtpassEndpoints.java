package com.sq018.monieflex.utils;

import org.springframework.beans.factory.annotation.Value;

public class VtpassEndpoints {
    private static String BASE_URL = "https://sandbox.vtpass.com/api";

    /**
     * ServiceID = gotv, dstv, startimes, showmax
     */
    public static String VARIATION_URL(String id) {
        return BASE_URL + "/service-variations?serviceID=%s".formatted(id);
    };
}
