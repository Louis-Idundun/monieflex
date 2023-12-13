package com.sq018.monieflex.utils;

public class VtpassEndpoints {
    private static final String BASE_URL = "https://sandbox.vtpass.com/api";

    /**
     * ServiceID(Tv) = gotv, dstv, startimes, showmax
     * ServiceID = gotv, dstv, startimes, showmax
     */
    public static String VARIATION_URL(String id) {
        return BASE_URL + "/service-variations?serviceID=%s".formatted(id);
    }

    public static String PAY = BASE_URL + "/pay";
}
