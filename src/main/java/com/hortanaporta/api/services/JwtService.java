package com.hortanaporta.api.services;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtService {

    private final SecretKey secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private final long expirationTime = 86400000; // 24 horas

    public String generateToken(String email, Long userId, String role) {
    System.out.println("=== DEBUG JWT GENERATION ===");
    System.out.println("Email: " + email);
    System.out.println("UserId: " + userId);
    System.out.println("Role: " + role); // ← VERIFIQUE SE ESTÁ CHEGANDO AQUI
    
    return Jwts.builder()
            .setSubject(email)
            .claim("userId", userId)
            .claim("role", role) // ← ESTÁ AQUI, MAS PODE ESTAR NULL
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
            .signWith(secretKey)
            .compact();
}

    public String extractEmail(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public Long extractUserId(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("userId", Long.class);
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}