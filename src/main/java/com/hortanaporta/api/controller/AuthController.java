package com.hortanaporta.api.controller;

import com.hortanaporta.api.model.Pessoa;
import com.hortanaporta.api.services.JwtService;
import com.hortanaporta.api.services.PessoaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    System.out.println("=== DEBUG INICIADO ===");
    System.out.println("DADOS RECEBIDOS: " + dados);
    
    // Extrai email e senha
    String email = null;
    String senha = null;
    
    if (dados.containsKey("EmailPessoa")) {
        email = (String) dados.get("EmailPessoa");
        System.out.println("Email encontrado: " + email);
    } else {
        System.out.println("EmailPessoa NÃO encontrado nas chaves: " + dados.keySet());
    }
    
    if (dados.containsKey("SenhaPessoa")) {
        senha = (String) dados.get("SenhaPessoa");
        System.out.println("Senha encontrada: " + senha);
    } else {
        System.out.println("SenhaPessoa NÃO encontrado nas chaves: " + dados.keySet());
    }
    
    if (email == null || senha == null) {
        System.out.println("=== ERRO: Email ou senha são null ===");
        return ResponseEntity.badRequest().body(Map.of("error", "Email e senha são obrigatórios"));
    }
    
    System.out.println("Chamando autenticação...");
    Optional<Pessoa> pessoaOpt = pessoaService.autenticar(email, senha);
    
    if (pessoaOpt.isPresent()) {
        Pessoa pessoa = pessoaOpt.get();
        String token = jwtService.generateToken(pessoa.getEmail(), pessoa.getId());
        System.out.println("=== LOGIN BEM-SUCEDIDO ===");
        
        return ResponseEntity.ok(Map.of(
            "token", token,
            "usuario", pessoa,
            "message", "Login realizado com sucesso"
        ));
    }
    
    System.out.println("=== LOGIN FALHOU ===");
    return ResponseEntity.status(401).body(Map.of("error", "Email ou senha inválidos"));
}

    @PostMapping("/registrar")
public ResponseEntity<?> registrar(@RequestBody Pessoa pessoa) {
    try {
        System.out.println("=== DADOS RECEBIDOS NO REGISTRO ===");
        System.out.println("Nome: " + pessoa.getNome());
        System.out.println("Email: " + pessoa.getEmail());
        System.out.println("CPF: " + pessoa.getCpf());
        System.out.println("Senha: " + (pessoa.getSenha() != null ? "***" : "null"));
        
        // Verifica se email já existe
        if (pessoaService.buscarPorEmail(pessoa.getEmail()) != null) {
            System.out.println("ERRO: Email já cadastrado");
            return ResponseEntity.badRequest().body(Map.of("error", "Email já cadastrado"));
        }
        
        Pessoa pessoaSalva = pessoaService.salvar(pessoa);
        String token = jwtService.generateToken(pessoaSalva.getEmail(), pessoaSalva.getId());
        
        System.out.println("SUCESSO: Usuário criado - ID: " + pessoaSalva.getId());
        
        return ResponseEntity.ok(Map.of(
            "token", token,
            "usuario", pessoaSalva,
            "message", "Usuário criado com sucesso"
        ));
    } catch (Exception e) {
        System.out.println("ERRO NO REGISTRO: " + e.getMessage());
        e.printStackTrace();
        return ResponseEntity.badRequest().body(Map.of("error", "Erro ao criar usuário: " + e.getMessage()));
    }
}

    // Classe interna para receber dados de login
    public static class LoginRequest {
        private String email;
        private String senha;

        // Getters e Setters
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getSenha() { return senha; }
        public void setSenha(String senha) { this.senha = senha; }
    }
}