package com.hortanaporta.api.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "tb_item_pedido")
@Data
public class ItemPedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cd_item_pedido")
    @JsonProperty("cd_item_pedido")
    private Long id;

    // ✅ CORRIGIDO: ManyToOne com fetch EAGER e nullable false
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cd_pedido", nullable = false)
    @JsonIgnore // Evita recursão infinita no JSON
    private Pedido pedido;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cd_produto", nullable = false)
    @JsonProperty("produto")
    private Produto produto;

    @Column(name = "quantidade", nullable = false)
    @JsonProperty("quantidade")
    private Integer quantidade;

    @Column(name = "preco_unitario", nullable = false)
    @JsonProperty("preco_unitario")
    private Double precoUnitario;

    @Column(name = "subtotal", nullable = false)
    @JsonProperty("subtotal")
    private Double subtotal;

    @PrePersist
    @PreUpdate
    public void calcularSubtotal() {
        if (this.precoUnitario != null && this.quantidade != null) {
            this.subtotal = this.precoUnitario * this.quantidade;
        } else {
            this.subtotal = 0.0;
        }
    }
}