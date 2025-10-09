package com.hortanaporta.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "tb_pedido")
@Data
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cd_pedido")
    @JsonProperty("cd_pedido")
    private Long id;

    @Column(name = "data_pedido", nullable = false)
    @JsonProperty("data_pedido")
    private LocalDateTime dataPedido;

    @Column(name = "cd_pessoa", nullable = false)
    @JsonProperty("cd_pessoa")
    private Long cdPessoa;

    @Column(name = "status_pedido", nullable = false)
    @JsonProperty("status_pedido")
    private String statusPedido = "PENDENTE";

    @Column(name = "forma_pagamento", nullable = false)
    @JsonProperty("forma_pagamento")
    private String formaPagamento;

    @Column(name = "valor_total", nullable = false)
    @JsonProperty("valor_total")
    private Double valorTotal;

    // ✅ CHAVE ESTRANGEIRA PARA ENDEREÇO
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cd_endereco", nullable = false)
    @JsonProperty("endereco_entrega")
    private Endereco enderecoEntrega;

    // Campo para armazenar os itens do pedido como JSON
    @Column(name = "itens_pedido", columnDefinition = "TEXT")
    @JsonProperty("itens_pedido")
    private String itensPedido;

    public Pedido() {
        this.dataPedido = LocalDateTime.now();
    }
}