package com.ibgs.studyAssistant.auth.controller;

import com.ibgs.studyAssistant.auth.model.User;
import com.ibgs.studyAssistant.auth.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {


    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody @Valid User user){
        return authService.login(user);
    }

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody @Valid User user) {
        User userSave = authService.register(user);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
