package com.sq018.monieflex.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

public record AirtimeDto (String network,
                          Integer amount,
                          @JsonProperty("phone_number")
                          String phoneNumber){
}
