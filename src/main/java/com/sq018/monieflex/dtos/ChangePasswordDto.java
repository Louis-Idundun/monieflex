package com.sq018.monieflex.dtos;

public record ChangePasswordDto(String oldPassword, String newPassword, String confirmPassword) {
}