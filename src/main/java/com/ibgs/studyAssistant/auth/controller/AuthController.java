package com.ibgs.studyAssistant.auth.controller;

import com.ibgs.studyAssistant.auth.dto.AuthMeResponse;
import com.ibgs.studyAssistant.auth.dto.LoginRequest;
import com.ibgs.studyAssistant.auth.dto.LoginResponse;
import com.ibgs.studyAssistant.auth.dto.RefreshTokenRequest;
import com.ibgs.studyAssistant.auth.model.User;
import com.ibgs.studyAssistant.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {


    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        return authService.login(request);
    }

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody @Valid User user) {
        User userSave = authService.register(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(userSave);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<LoginResponse> refreshToken(@RequestBody RefreshTokenRequest request) {
        return authService.refreshToken(request);
    }

    @GetMapping("/me")
    public ResponseEntity<AuthMeResponse> me() {
        AuthMeResponse authMeResponse = authService.getCurrentUser();
        return ResponseEntity.ok().body(authMeResponse);
    }

}
