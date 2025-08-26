package com.hortanaporta.api.services;
import org.springframework.stereotype.Service;
import java.util.List;
import  com.hortanaporta.api.repository.*;
import  com.hortanaporta.api.model.*;

@Service
public class PessoaService {

    private final PessoaRepository pessoaRepository;

    public PessoaService(PessoaRepository pessoaRepository) {
        this.pessoaRepository = pessoaRepository;
    }

    public List<Pessoa> listarTodos() {
        return pessoaRepository.findAll();
    }

    public Pessoa buscarPorId(Long id) {
        return pessoaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pessoa não encontrado"));
    }

    public Pessoa salvar(Pessoa pessoa) {
        return pessoaRepository.save(pessoa);
    }

    public void excluir(Long id) {
        pessoaRepository.deleteById(id);
    }
}