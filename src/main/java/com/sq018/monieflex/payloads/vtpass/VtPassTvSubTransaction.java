package com.sq018.monieflex.payloads.vtpass;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class VtPassTvSubTransaction {
    private String status;
    private String transactionId;
}
