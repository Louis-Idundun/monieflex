package com.sq018.monieflex.payloads.vtpass;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VtPassAirtimeTransactionDate {
    @JsonProperty("date")
    public String date;
    @JsonProperty("timezone_type")
    public Integer timezoneType;
    @JsonProperty("timezone")
    public String timezone;
}
