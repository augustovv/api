package com.hortanaporta.api.repository;
import com.hortanaporta.api.model.Pessoa;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PessoaRepository extends JpaRepository<Pessoa, Long> {
    Pessoa findByEmail(String email);
    Pessoa findByCpf(String cpf);
} 
