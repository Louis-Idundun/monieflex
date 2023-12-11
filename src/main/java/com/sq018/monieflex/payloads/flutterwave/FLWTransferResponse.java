package com.sq018.monieflex.payloads.flutterwave;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FLWTransferResponse {
    private String status;
    private String message;
    private TransferResponse data;
}
