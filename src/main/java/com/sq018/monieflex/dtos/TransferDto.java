package com.sq018.monieflex.dtos;

public record TransferDto(
        String bankCode,
        String accountNumber,
        Integer amount,
        String narration
) {
}
