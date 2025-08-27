package com.hortanaporta.api.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;


import com.hortanaporta.api.model.Produto;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {
    
    // Use String em vez de Integer
    List<Produto> findByCategoria(String categoria);
}
