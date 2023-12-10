package com.sq018.monieflex.controllers;


import com.sq018.monieflex.dtos.LoginDto;
import com.sq018.monieflex.dtos.SignupDto;
import com.sq018.monieflex.enums.VerifyType;
import com.sq018.monieflex.payloads.ApiResponse;
import com.sq018.monieflex.payloads.LoginResponse;
import com.sq018.monieflex.services.AuthService;
import com.sq018.monieflex.services.implementations.AuthImplementation;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    @Autowired
    public AuthController(AuthImplementation authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@RequestBody LoginDto login) {
        return authService.login(login);
    }

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<String>> signup(@RequestBody SignupDto signup) {
        return authService.signup(signup);
    }

    @GetMapping("/confirm-email-address")
    public String confirmEmailAddress(@RequestParam String token) {
        return authService.confirmEmail(token);
    }

    @GetMapping("/resend-link")
    public ResponseEntity<ApiResponse<String>> resendLink(@RequestParam String email, @RequestParam VerifyType type) {
        return authService.resendLink(email, type);
    }

    @GetMapping("/check-email-for-password-reset")
    public ResponseEntity<ApiResponse<String>> checkEmailForPasswordReset(@RequestParam String emailAddress) {
        return authService.checkEmailForPasswordReset(emailAddress);
    }

    @GetMapping("/verify-email-address")
    public String verifyResetPasswordLink(@RequestParam String token, HttpServletResponse response) {
        return authService.verifyResetPasswordLink(token, response);
    }

    public record ResetPassword(String newPassword, String confirmPassword) {}

    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponse<String>> resetPassword(
            @RequestParam String token, @RequestBody ResetPassword body
    ) {
        return authService.resetPassword(token, body.newPassword(), body.confirmPassword());
    }
}
