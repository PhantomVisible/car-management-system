package com.carmanagement.auth.infrastructure.controllers;

import com.carmanagement.auth.application.services.AuthService;
import com.carmanagement.auth.domain.models.User;
import com.carmanagement.auth.shared.dtos.AuthResponse;
import com.carmanagement.auth.shared.dtos.RegisterRequest;
import com.carmanagement.auth.shared.dtos.*;
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
        // Authenticate and generate JWT
        String token = authService.authenticateAndGenerateToken(request.getEmail(), request.getPassword());

        User user = authService.loginUser(request.getEmail(), request.getPassword());

        AuthResponse response = new AuthResponse(
                user.getEmail(),
                user.getRole().name(),
                "Login successful"
        );
        response.setToken(token);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<AuthUserResponse> getUserById(@PathVariable Long id) {
        User user = authService.findUserById(id);
        AuthUserResponse response = new AuthUserResponse(
                user.getUserId(),
                user.getEmail(),
                user.getRole().name()
        );
        return ResponseEntity.ok(response);
    }

}
