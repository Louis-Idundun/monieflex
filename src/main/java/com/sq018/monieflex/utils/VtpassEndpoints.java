package com.sq018.monieflex.utils;


public class VtpassEndpoints {
    private static final String BASE_URL = "https://sandbox.vtpass.com/api";

    /**
     * ServiceID(Tv) = gotv, dstv, startimes, showmax
     * ServiceID = gotv, dstv, startimes, showmax
     * ServiceID(Data) = mtn, glo, airtel, nine-mobile
     * ServiceID = ikeja-electric, eko-electric, kano-electric, portharcourt-electric, jos-electric,
     * ibadan-electric, kaduna-electric, abuja-electric, enugu-electric, benin-electric, aba-electric
     */

    public static String VARIATION_URL(String id) {
        return BASE_URL + "/service-variations?serviceID=%s".formatted(id);
    };

    public static String PAY = BASE_URL + "/pay";


    public static String VERIFY_NUMBER = BASE_URL + "/merchant-verify";

  //  public static  String VERIFY_NUMBER = BASE_URL + "/merchant-verify";
}
