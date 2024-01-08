package com.sq018.monieflex.dtos;

import com.sq018.monieflex.entities.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * DTO for {@link User}
 */
public record UserDto(
      @NotEmpty(message = "First name should not be empty") String firstName,
      @NotEmpty(message = "Last name should not be empty") String lastName,
      @NotEmpty(message = "Password should not be empty") String password,
      @Email(message = "Email must be properly formatted") String emailAddress,
      @NotEmpty(message = "Phone number should not be empty") String phoneNumber,
      @Min(message = "BVN should not be less than 2 characters", value = 2) BigDecimal bvn
) implements Serializable {
}