package com.sq018.monieflex.services;

import com.sq018.monieflex.dtos.LoginDto;
import com.sq018.monieflex.dtos.SignupDto;
import com.sq018.monieflex.enums.VerifyType;
import com.sq018.monieflex.payloads.ApiResponse;
import com.sq018.monieflex.payloads.LoginResponse;
import org.springframework.http.ResponseEntity;

public interface AuthService {
    ResponseEntity<ApiResponse<LoginResponse>> login(LoginDto loginDto);
    ResponseEntity<ApiResponse<String>> signup(SignupDto signupDto);
    ApiResponse<String> confirmEmail(String token);
    ResponseEntity<ApiResponse<String>> resendLink(String email, VerifyType type);
    ResponseEntity<ApiResponse<String>> checkEmailForPasswordReset(String emailAddress);
    ResponseEntity<ApiResponse<String>> resetPassword(String token, String password, String confirmPassword);
}
