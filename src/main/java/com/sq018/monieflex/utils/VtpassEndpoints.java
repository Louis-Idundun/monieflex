package com.sq018.monieflex.utils;

public class VtpassEndpoints {

    private static String BASE_URL = "https://sandbox.vtpass.com/api";

    /**
     * ServiceID(Data) = mtn, glo, airtel, nine-mobile
     */

    public static String VARIATION_URL(String id) {
        return BASE_URL + "/service-variations?serviceID=%s".formatted(id);
    }
}
