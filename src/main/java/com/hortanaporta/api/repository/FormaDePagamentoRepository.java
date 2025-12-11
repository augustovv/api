package com.hortanaporta.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hortanaporta.api.model.forma_de_pagamento;


@Repository
public interface FormaDePagamentoRepository extends JpaRepository<forma_de_pagamento, Long> {
   
}

