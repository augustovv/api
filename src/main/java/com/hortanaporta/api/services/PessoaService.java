package com.hortanaporta.api.services;

import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import com.hortanaporta.api.repository.*;
import com.hortanaporta.api.model.*;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
public class PessoaService {

    private final PessoaRepository pessoaRepository;
    private final PasswordEncoder passwordEncoder;

    public PessoaService(PessoaRepository pessoaRepository, PasswordEncoder passwordEncoder) {
        this.pessoaRepository = pessoaRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<Pessoa> listarTodos() {
        return pessoaRepository.findAll();
    }

    public Pessoa buscarPorId(Long id) {
        return pessoaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pessoa não encontrado"));
    }

    public Pessoa salvar(Pessoa pessoa) {
        // Criptografa a senha antes de salvar
        if (pessoa.getSenha() != null && !pessoa.getSenha().startsWith("$2a$")) {
            pessoa.setSenha(passwordEncoder.encode(pessoa.getSenha()));
        }
        return pessoaRepository.save(pessoa);
    }

    public void excluir(Long id) {
        pessoaRepository.deleteById(id);
    }

    // MÉTODO CORRIGIDO: Agora funciona com senhas em texto puro E BCrypt
    public Optional<Pessoa> autenticar(String email, String senha) {
    Pessoa pessoa = pessoaRepository.findByEmail(email);
    
    if (pessoa != null) {
        String senhaArmazenada = pessoa.getSenha();
        
        System.out.println("=== TENTATIVA DE LOGIN ===");
        System.out.println("Email: " + email);
        System.out.println("Senha fornecida: " + senha);
        System.out.println("Senha armazenada: " + senhaArmazenada);
        System.out.println("É BCrypt?: " + senhaArmazenada.startsWith("$2a$"));
        
        // Se a senha no banco É BCrypt, usa passwordEncoder.matches()
        if (senhaArmazenada.startsWith("$2a$")) {
            System.out.println("Validando com BCrypt...");
            if (passwordEncoder.matches(senha, senhaArmazenada)) {
                System.out.println("BCrypt: SENHA VÁLIDA");
                return Optional.of(pessoa);
            } else {
                System.out.println("BCrypt: SENHA INVÁLIDA");
            }
        } 
        // Se a senha no banco é TEXTO PURO, compara diretamente
        else if (senhaArmazenada.equals(senha)) {
            System.out.println("Texto puro: SENHA VÁLIDA");
            // Atualiza para BCrypt automaticamente
            pessoa.setSenha(passwordEncoder.encode(senha));
            pessoaRepository.save(pessoa);
            System.out.println("Senha migrada para BCrypt");
            return Optional.of(pessoa);
        } else {
            System.out.println("Texto puro: SENHA INVÁLIDA");
        }
    } else {
        System.out.println("USUÁRIO NÃO ENCONTRADO: " + email);
    }
    
    return Optional.empty();
}

    public Pessoa buscarPorEmail(String email) {
        return pessoaRepository.findByEmail(email);
    }
}