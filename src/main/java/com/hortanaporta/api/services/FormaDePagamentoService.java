package com.hortanaporta.api.services;
import com.hortanaporta.api.model.forma_de_pagamento;
import com.hortanaporta.api.repository.FormaDePagamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;  

@Service
public class FormaDePagamentoService {

    @Autowired
    private final FormaDePagamentoRepository FormaDePagamentoRepository;
    public FormaDePagamentoService(FormaDePagamentoRepository formaDePagamentoRepository) {
        this.FormaDePagamentoRepository = formaDePagamentoRepository;
    }

    public forma_de_pagamento buscarPorId(Long id) {
            return FormaDePagamentoRepository.findById(id).orElse(null);
}}

