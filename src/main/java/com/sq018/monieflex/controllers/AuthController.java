package com.sq018.monieflex.controllers;


import com.sq018.monieflex.dtos.LoginDto;
import com.sq018.monieflex.dtos.SignupDto;
import com.sq018.monieflex.payloads.ApiResponse;
import com.sq018.monieflex.services.AuthService;
import com.sq018.monieflex.services.implementations.AuthImplementation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    @Autowired
    public AuthController(AuthImplementation authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<String>> login(@RequestBody LoginDto login) {
        return authService.login(login);
    }

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<String>> signup(@RequestBody SignupDto signup) {
        return authService.signup(signup);
    }
}
