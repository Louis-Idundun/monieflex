package com.sq018.monieflex.payloads.flutterwave;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class FLWVerifyAccountResponse {
    private String status;
    private String message;
    private VerifyAccountResponse data;
}
