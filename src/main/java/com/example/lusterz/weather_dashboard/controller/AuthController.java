package com.example.lusterz.weather_dashboard.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.lusterz.weather_dashboard.dto.LoginRequest;
import com.example.lusterz.weather_dashboard.service.AuthService;



@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }
    
    @PostMapping
    public ResponseEntity<String> authenticateUser(@RequestBody LoginRequest loginRequest) {
        try {
            authService.authenticateUser(loginRequest);
            return ResponseEntity.ok("user authenticated");
        } catch (Exception e) {
            return ResponseEntity.status(401).body("login failed : " + e.getMessage());
        }
    }
}
