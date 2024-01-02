package com.sq018.monieflex.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sq018.monieflex.enums.BillType;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record AirtimeDto (
        BillType network,
        Integer amount,
        @JsonProperty("phone_number")
        @Size(min = 11, max= 11, message = "Phone number must be more than 11")
        String phoneNumber,
        String narration,
        @JsonProperty("beneficiary_name")
        String beneficiaryName
){
}
