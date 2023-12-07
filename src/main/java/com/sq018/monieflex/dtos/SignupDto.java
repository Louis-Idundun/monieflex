package com.sq018.monieflex.dtos;

public record SignupDto(
        String emailAddress,
        String bvn,
        String firstName,
        String lastName,
        String phoneNumber,
        String password
) {
}
