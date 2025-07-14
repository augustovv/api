package com.hortanaporta.api.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hortanaporta.api.model.Produto;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {
    @Query("SELECT p FROM TbProduto p WHERE p.categoria.id = :categoriaId")
    List<Produto> buscarPorCategoriaId(@Param("categoriaId") Long categoriaId);
}