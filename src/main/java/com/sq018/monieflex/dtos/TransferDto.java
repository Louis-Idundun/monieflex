package com.sq018.monieflex.dtos;

public record TransferDto(
        String bankCode,
        String accountNumber,
        String receiverName,
        String bankName,
        Integer amount,
        String narration
) {
}
