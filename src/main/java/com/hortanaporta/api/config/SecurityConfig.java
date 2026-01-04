package com.hortanaporta.api.config;

import com.hortanaporta.api.services.JwtService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtService jwtService;

    public SecurityConfig(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .csrf(csrf -> csrf.disable())
        .cors(cors -> cors.configure(http))
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(auth -> auth
            // 🔓 ENDPOINTS PÚBLICOS
            .requestMatchers("/api/auth/**").permitAll()
            .requestMatchers("/api/pessoas").permitAll()
            .requestMatchers("/api/enderecos/cep/**").permitAll()
            
            // 📧 ENDPOINTS DE EMAIL PÚBLICOS
            .requestMatchers("/api/email/test").permitAll()              // Teste
            .requestMatchers("/api/email/enviar").permitAll()            // Enviar email
            .requestMatchers("/api/email/confirmacao-pedido").permitAll() // Confirmação
            .requestMatchers("/api/email/recuperacao-senha").permitAll()  // Recuperação
            
            // 🔐 TODO O RESTO DA API PRECISA DE AUTENTICAÇÃO
            .requestMatchers("/api/**").authenticated()
            
            .anyRequest().permitAll()
        )
        .addFilterBefore(new JwtAuthenticationFilter(jwtService), UsernamePasswordAuthenticationFilter.class);

    return http.build();
}

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}