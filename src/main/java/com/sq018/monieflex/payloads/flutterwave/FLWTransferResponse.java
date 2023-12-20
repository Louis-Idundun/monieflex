package com.sq018.monieflex.payloads.flutterwave;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class FLWTransferResponse {
    private String status;
    private String message;
    private TransferResponse data;
}
