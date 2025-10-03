package com.example.Qolzy.controller;

import com.example.Qolzy.model.ApiResponse;
import com.example.Qolzy.model.auth.*;
import com.example.Qolzy.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@RequestBody LoginRequest loginRequest) {
        return authService.login(loginRequest);
    }

    @PostMapping("/firebase")
    public ResponseEntity<ApiResponse<LoginResponse>> loginOrRegisterWithFirebase(@RequestBody LoginRequestFirebase loginRequestFirebase) {
        return authService.loginOrRegisterWithFirebase(loginRequestFirebase);
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<String>> register(@RequestBody RegisterRequest registerRequest) {
        return authService.register(registerRequest);
    }

    @PostMapping("/refreshToken")
    public ResponseEntity<ApiResponse<RefreshTokenRequest>> refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        return authService.refreshToken(refreshTokenRequest);
    }
}
