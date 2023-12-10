package com.sq018.monieflex.services;

import com.sq018.monieflex.dtos.LoginDto;
import com.sq018.monieflex.dtos.SignupDto;
import com.sq018.monieflex.enums.VerifyType;
import com.sq018.monieflex.payloads.ApiResponse;
import com.sq018.monieflex.payloads.LoginResponse;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;

public interface AuthService {
    ResponseEntity<ApiResponse<LoginResponse>> login(LoginDto loginDto);
    ResponseEntity<ApiResponse<String>> signup(SignupDto signupDto);
    String confirmEmail(String token);
    ResponseEntity<ApiResponse<String>> checkEmailForPasswordReset(String emailAddress);
    String verifyResetPasswordLink(String token, HttpServletResponse response);
    ResponseEntity<ApiResponse<String>> resetPassword(
            String token, String password, String confirmPassword
    );
    ResponseEntity<ApiResponse<String>> resendLink(String email, VerifyType type);
}
