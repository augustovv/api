package com.hortanaporta.api.config;

import com.hortanaporta.api.services.JwtService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.lang.NonNull;

// **ADICIONE ESTES IMPORTS:**
import java.io.IOException;
import java.util.List;                    // ← ESTE ESTAVA FALTANDO
import java.util.Collections;             // ← E ESTE TAMBÉM

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    public JwtAuthenticationFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(
        @NonNull HttpServletRequest request,
        @NonNull HttpServletResponse response, 
        @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        
        String authHeader = request.getHeader("Authorization");
        
        System.out.println("=== DEBUG JWT FILTER ===");
        System.out.println("URL: " + request.getRequestURL());
        System.out.println("Authorization Header: " + authHeader);
        
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            System.out.println("Token recebido: " + (token.length() > 20 ? token.substring(0, 20) + "..." : token));
            
            boolean isValid = jwtService.validateToken(token);
            System.out.println("Token válido? " + isValid);
            
            if (isValid) {
                String email = jwtService.extractEmail(token);
                String role = jwtService.extractRole(token);
                
                System.out.println("Email extraído: " + email);
                System.out.println("Role extraída: " + role);
                
                // AGORA VAI FUNCIONAR - os imports estão corretos
                List<SimpleGrantedAuthority> authorities = Collections.singletonList(
                    new SimpleGrantedAuthority("ROLE_" + role)
                );
                
                UsernamePasswordAuthenticationToken authentication = 
                    new UsernamePasswordAuthenticationToken(email, null, authorities);
                
                SecurityContextHolder.getContext().setAuthentication(authentication);
                System.out.println("✅ Autenticação configurada com sucesso para role: ROLE_" + role);
            } else {
                System.out.println("❌ Token inválido");
            }
        } else {
            System.out.println("❌ Header Authorization faltando ou mal formatado");
        }
        
        System.out.println("=== FIM DEBUG JWT FILTER ===");
        
        filterChain.doFilter(request, response);
    }
}