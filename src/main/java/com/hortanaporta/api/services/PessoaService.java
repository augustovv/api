package com.hortanaporta.api.services;

import org.springframework.stereotype.Service;

import java.util.List;

import  com.hortanaporta.api.repository.*;
import  com.hortanaporta.api.model.*;


@Service
public class PessoaService {

    private final PessoaRepository PessoaRepository;

    public PessoaService(PessoaRepository PessoaRepository) {
        this.PessoaRepository = PessoaRepository;
    }

    public List<Pessoa> listarTodos() {
        return PessoaRepository.findAll();
    }

    public Pessoa buscarPorId(Long id) {
        return PessoaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pessoa não encontrado"));
    }

    public Pessoa salvar(Pessoa Pessoa) {
        return PessoaRepository.save(Pessoa);
    }

    public void excluir(Long id) {
        PessoaRepository.deleteById(id);
    }


}