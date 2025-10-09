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

    // NO PessoaService.java - ATUALIZE o método autenticar
    public Optional<Pessoa> autenticar(String email, String senha) {
        System.out.println("=== DEBUG AUTENTICAÇÃO ===");

        Pessoa pessoa = buscarPorEmail(email);
        if (pessoa == null) {
            System.out.println("Pessoa não encontrada para email: " + email);
            return Optional.empty();
        }

        System.out.println("Pessoa encontrada - Role: '" + pessoa.getRole() + "'");

        if (passwordEncoder.matches(senha, pessoa.getSenha())) {
            System.out.println("Senha correta - Role final: '" + pessoa.getRole() + "'");
            return Optional.of(pessoa); // ← NÃO CRIE NOVA INSTÂNCIA!
        } else {
            System.out.println("Senha incorreta");
            return Optional.empty();
        }
    }

    public Pessoa buscarPorEmail(String email) {
        return pessoaRepository.findByEmail(email);
    }
}