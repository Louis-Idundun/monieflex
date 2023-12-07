package com.sq018.monieflex.payloads.flutterwave;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class FLWVirtualAccountResponse {
    private String message;
    private String status;
    private VirtualAccountResponse data;
}
