package com.sq018.monieflex.dtos;

public record VtpassTvSubscriptionDto(

        String requestId,
        String serviceid,
        String billerCode,
        String variationCode,
        Integer amount,
        String phone,
        String subcriptionType

)


 {
}
