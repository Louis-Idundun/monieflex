package com.sq018.monieflex.controllers;

import com.sq018.monieflex.payloads.ApiResponse;
import com.sq018.monieflex.payloads.ProfileResponse;
import com.sq018.monieflex.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    @GetMapping("/profile")
    public ResponseEntity<ApiResponse<ProfileResponse>> viewProfile() {
        ApiResponse<ProfileResponse> apiResponse = userService.viewProfile();
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }
}
