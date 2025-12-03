package com.carmanagement.auth.shared.dtos;

public class AuthUserResponse {
    private Long id;
    private String email;
    private String role;

    public AuthUserResponse(Long id, String email, String role) {
        this.id = id;
        this.email = email;
        this.role = role;
    }

    // getters
    public Long getId() { return id; }
    public String getEmail() { return email; }
    public String getRole() { return role; }
}