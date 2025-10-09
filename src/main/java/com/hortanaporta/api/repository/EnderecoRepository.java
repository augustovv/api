package com.hortanaporta.api.repository;

import com.hortanaporta.api.model.Endereco;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EnderecoRepository extends JpaRepository<Endereco, Long> {
    
    // Buscar endereços por pessoa
    List<Endereco> findByCdPessoa(Long cdPessoa);
    
    // Buscar endereço principal de uma pessoa
    Optional<Endereco> findByCdPessoaAndEnderecoPrincipalTrue(Long cdPessoa);
    
    // Verificar se CEP já existe para a pessoa
    boolean existsByCepAndCdPessoa(String cep, Long cdPessoa);
    
    // Buscar por CEP (para consulta)
    List<Endereco> findByCep(String cep);
}