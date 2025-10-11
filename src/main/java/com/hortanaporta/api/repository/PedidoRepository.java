package com.hortanaporta.api.repository;

import com.hortanaporta.api.model.Pedido;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {
   
}