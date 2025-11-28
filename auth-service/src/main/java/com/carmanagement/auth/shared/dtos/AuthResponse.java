package com.carmanagement.auth.shared.dtos;

public class AuthResponse {
    private String email;
    private String role;
    private String message;
    private String token; // We'll add this later with JWT

    // Constructors
    public AuthResponse() {}

    public AuthResponse(String email, String role, String message) {
        this.email = email;
        this.role = role;
        this.message = message;
    }

    // Getters and Setters
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
}