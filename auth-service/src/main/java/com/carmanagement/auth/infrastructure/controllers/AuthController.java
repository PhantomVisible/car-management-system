package com.carmanagement.auth.infrastructure.controllers;

import com.carmanagement.auth.application.services.AuthService;
import com.carmanagement.auth.domain.models.Role;
import com.carmanagement.auth.domain.models.User;
import com.carmanagement.auth.shared.dtos.AuthResponse;
import com.carmanagement.auth.shared.dtos.RegisterRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        User user = authService.registerUser(
                request.getEmail(),
                request.getPassword(),
                request.getRole()
        );

        AuthResponse response = new AuthResponse(
                user.getEmail(),
                user.getRole().name(),
                "User registered successfully"
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody RegisterRequest request) {

        String token = authService.authenticateAndGenerateToken(request.getEmail(), request.getPassword());
        User user = authService.loginUser(request.getEmail(), request.getPassword());

        AuthResponse response = new AuthResponse(
                request.getEmail(),
                "USER", // We'll get actual role from token later
                "Login successful"
        );
        response.setToken(token);
        // We'll add JWT token in the next step

        return ResponseEntity.ok(response);
    }
}