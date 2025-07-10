package com.hortanaporta.api.controller;

import com.hortanaporta.api.services.*;
import com.hortanaporta.api.model.*;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/Pessoas")
public class PessoaController {

    private final PessoaService PessoaService;

    public PessoaController(PessoaService PessoaService) {
        this.PessoaService = PessoaService;
    }

    @GetMapping
    public List<Pessoa> listarTodos() {
        return PessoaService.listarTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pessoa> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(PessoaService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<Pessoa> criar(@RequestBody Pessoa Pessoa) {
        return ResponseEntity.ok(PessoaService.salvar(Pessoa));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Pessoa> atualizar(@PathVariable Long id, @RequestBody Pessoa Pessoa) {
        Pessoa.setId(id);
        return ResponseEntity.ok(PessoaService.salvar(Pessoa));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        PessoaService.excluir(id);
        return ResponseEntity.noContent().build();
    }


} 
