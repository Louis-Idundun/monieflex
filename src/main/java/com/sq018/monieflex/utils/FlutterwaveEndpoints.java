package com.sq018.monieflex.utils;

public class FlutterwaveEndpoints {
    private static final String BASE_URL = "https://api.flutterwave.com/v3/";

    public static final String VIRTUAL_ACCOUNT_NUMBER = BASE_URL + "virtual-account-numbers";

    /**
     * Param: country. NOTE: Attach country code to the endpoint. Eg: NG
     */
    public static final String GET_ALL_BANKS = BASE_URL + "banks/NG";

    public static final String TRANSFER = BASE_URL + "transfers";
}
