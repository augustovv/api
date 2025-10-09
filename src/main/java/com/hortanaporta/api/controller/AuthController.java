package com.hortanaporta.api.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hortanaporta.api.model.Pessoa;
import com.hortanaporta.api.services.JwtService;
import com.hortanaporta.api.services.PessoaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/auth")
public class AuthController {

    private final PessoaService pessoaService;
    private final JwtService jwtService;

    public AuthController(PessoaService pessoaService, JwtService jwtService) {
        this.pessoaService = pessoaService;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, Object> dados) {
        System.out.println("=== DEBUG COMPLETO DO LOGIN ===");
        System.out.println("Tipo dos dados: " + dados.getClass());
        System.out.println("Chaves recebidas: " + dados.keySet());

        // Log de todas as chaves e valores
        for (Map.Entry<String, Object> entry : dados.entrySet()) {
            System.out.println("Key: " + entry.getKey() + " | Value: " + entry.getValue() + " | Tipo: " +
                    (entry.getValue() != null ? entry.getValue().getClass() : "null"));
        }

        String email = (String) dados.get("EmailPessoa");
        String senha = (String) dados.get("SenhaPessoa");

        System.out.println("Email extraído: " + email);
        System.out.println("Senha extraída: " + senha);

        if (email == null || senha == null) {
            System.out.println("ERRO: Email ou senha são null");
            return ResponseEntity.badRequest().body(Map.of("error", "Email e senha são obrigatórios"));
        }

        Optional<Pessoa> pessoaOpt = pessoaService.autenticar(email, senha);

        if (pessoaOpt.isPresent()) {
        Pessoa pessoa = pessoaOpt.get();
        
        System.out.println("=== DEBUG CRÍTICO - VALOR REAL DO BANCO ===");
        System.out.println("RolePessoa do banco: '" + pessoa.getRole() + "'");
        System.out.println("É igual a 'ADMIN'? " + "ADMIN".equals(pessoa.getRole()));
        System.out.println("É igual a 'CLIENTE'? " + "CLIENTE".equals(pessoa.getRole()));
        
        // DEBUG: Verificar se há problema de case (maiúsculo/minúsculo)
        System.out.println("Role em maiúsculo: " + (pessoa.getRole() != null ? pessoa.getRole().toUpperCase() : "null"));
        
        // SOLUÇÃO IMEDIATA: Forçar o valor do banco
        String roleDoBanco = pessoa.getRole();
        System.out.println("Role que será usada: '" + roleDoBanco + "'");
        
        String token = jwtService.generateToken(
                pessoa.getEmail(),
                pessoa.getId(),
                roleDoBanco // ← Usar o valor do banco
        );

        Map<String, Object> response = Map.of(
                "token", token,
                "usuario", pessoa,
                "message", "Login realizado com sucesso"
        );
        
        return ResponseEntity.ok(response);
    }

        return ResponseEntity.status(401).body(Map.of("error", "Email ou senha inválidos"));
    }

    @PostMapping("/registrar")
    public ResponseEntity<?> registrar(@RequestBody Pessoa pessoa) {
        try {
            // Verifica se email já existe
            if (pessoaService.buscarPorEmail(pessoa.getEmail()) != null) {
                return ResponseEntity.badRequest().body(Map.of("error", "Email já cadastrado"));
            }

            Pessoa pessoaSalva = pessoaService.salvar(pessoa);

            // CORREÇÃO: Adicione o terceiro parâmetro (role)
            String token = jwtService.generateToken(
                    pessoaSalva.getEmail(),
                    pessoaSalva.getId(),
                    pessoaSalva.getRole() // ← TERCEIRO PARÂMETRO
            );

            return ResponseEntity.ok(Map.of(
                    "token", token,
                    "usuario", pessoaSalva,
                    "message", "Usuário criado com sucesso"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Erro ao criar usuário"));
        }
    }

    // Classe interna para receber dados de login
    public static class LoginRequest {
        private String email;
        private String senha;

        // Getters e Setters
        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getSenha() {
            return senha;
        }

        public void setSenha(String senha) {
            this.senha = senha;
        }
    }
}