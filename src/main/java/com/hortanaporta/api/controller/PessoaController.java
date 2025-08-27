package com.hortanaporta.api.controller;

import com.hortanaporta.api.services.*;

import jakarta.validation.Valid;

import com.hortanaporta.api.model.*;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/pessoas")
public class PessoaController {
    private final PessoaService pessoaService;

    public PessoaController(PessoaService pessoaService) {
        this.pessoaService = pessoaService;
    }

    @GetMapping
    public ResponseEntity<List<Pessoa>> listarTodos() {
        return ResponseEntity.ok(pessoaService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pessoa> buscarPorId(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(pessoaService.buscarPorId(id));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

   @PostMapping
    public ResponseEntity<Pessoa> criar(@RequestBody Map<String, Object> dados) {
    Pessoa pessoa = new Pessoa();
    pessoa.setNome((String) dados.get("NmPessoa"));
    pessoa.setCpf((String) dados.get("CpfPessoa"));
    pessoa.setEmail((String) dados.get("EmailPessoa"));
    pessoa.setSenha((String)dados.get("SenhaPessoa"));

    System.out.println("Seus dados enviados são: " + dados);
    
    return ResponseEntity.ok(pessoaService.salvar(pessoa));
}

    @PutMapping("/{id}")
    public ResponseEntity<Pessoa> atualizar(@PathVariable Long id, @Valid @RequestBody Pessoa pessoa) {
        try {
            pessoa.setId(id);
            return ResponseEntity.ok(pessoaService.salvar(pessoa));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        try {
            pessoaService.excluir(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}