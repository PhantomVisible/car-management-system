package com.carmanagement.rental.infrastructure.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

@Component
public class JwtUtil {

    @Value("${jwt.secret:defaultSecretKeyForRentalService}")
    private String jwtSecret;

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }

    public Claims extractClaims(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (Exception e) {
            throw new RuntimeException("Invalid JWT token: " + e.getMessage());
        }
    }

    public String extractEmail(String token) {
        return extractClaims(token).getSubject();
    }

    public String extractRole(String token) {
        Claims claims = extractClaims(token);
        return claims.get("role", String.class);
    }

    public Long extractUserId(String token) {
        try {
            Claims claims = extractClaims(token);

            // Try different possible claims for userId
            if (claims.get("userId") != null) {
                return claims.get("userId", Long.class);
            }
            if (claims.get("user_id") != null) {
                return claims.get("user_id", Long.class);
            }
            if (claims.get("sub") != null) {
                // If subject is numeric (userId as string)
                String subject = claims.getSubject();
                try {
                    return Long.parseLong(subject);
                } catch (NumberFormatException e) {
                    // Subject is email, not userId
                    return null;
                }
            }

            return null;

        } catch (Exception e) {
            throw new RuntimeException("Failed to extract userId from token: " + e.getMessage());
        }
    }

    public boolean isTokenValid(String token) {
        try {
            extractClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}