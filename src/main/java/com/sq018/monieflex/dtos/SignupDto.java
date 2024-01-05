package com.sq018.monieflex.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record SignupDto(
        @NotEmpty(message = "Email cannot be empty")
        @Email(message = "Email is not properly formatted")
        String emailAddress,
        @NotEmpty(message = "BVN cannot be empty")
        @Size(min = 11, max = 11, message = "BVN cannot be less or more than 11")
        String bvn,
        @NotEmpty(message = "First name cannot be empty")
        String firstName,
        @NotEmpty(message = "Last name cannot be empty")
        String lastName,
        @NotEmpty(message = "Phone number cannot be empty")
        @Size(min = 11, message = "Phone number cannot be less than 11")
        String phoneNumber,
        @NotEmpty(message = "Password cannot be empty")
        String password

) {
}
