package com.carmanagement.car.infrastructure.security;

import jakarta.ws.rs.HttpMethod;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.crypto.SecretKey;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true) // Enable @PreAuthorize
public class SecurityConfig {

    private final SecretKey secretKey;

    public SecurityConfig(SecretKey secretKey) {
        this.secretKey = secretKey;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(authz -> authz
                        // Make ALL car endpoints require authentication
                        .requestMatchers("/api/cars/**").authenticated()
                        .anyRequest().permitAll() // Other endpoints are public
                )
                .addFilterBefore(
                        new JwtAuthenticationFilter(secretKey),
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }
}