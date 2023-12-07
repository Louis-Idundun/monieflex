package com.sq018.monieflex.services;

import com.sq018.monieflex.dtos.LoginDto;
import com.sq018.monieflex.dtos.SignupDto;
import com.sq018.monieflex.payloads.ApiResponse;
import org.springframework.http.ResponseEntity;

public interface AuthService {
    ResponseEntity<ApiResponse<String>> login(LoginDto loginDto);
    ResponseEntity<ApiResponse<String>> signup(SignupDto signupDto);
}
