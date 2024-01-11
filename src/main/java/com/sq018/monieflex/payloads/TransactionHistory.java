package com.sq018.monieflex.payloads;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sq018.monieflex.enums.TransactionStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TransactionHistory {
        private String name;
        private String description;
        private String amount;
        private String date;
        private String time;
        private Boolean isCredit;
        private TransactionStatus status;
        private Integer pages;
}
