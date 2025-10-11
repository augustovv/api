package com.hortanaporta.api.model;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "tb_pedido")
@Data
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cd_pedido")
    @JsonProperty("cd_pedido")
    private Long cd_pedido;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cd_pessoa", nullable = false)
    @JsonProperty("pessoa")
    private Pessoa pessoa;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cd_endereco", nullable = false)
    @JsonProperty("endereco_entrega")
    private Endereco enderecoEntrega;

    @Column(name = "data_pedido", nullable = false)
    @JsonProperty("data_pedido")
    private LocalDateTime dataPedido;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonProperty("itens_pedido")
    private List<ItemPedido> itensPedido = new ArrayList<>();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "forma_de_pagamento", nullable = false)
    @JsonProperty("forma_de_pagamento")
    private forma_de_pagamento forma_de_pagamento;

    @Column(name = "observacoes")
    @JsonProperty("observacoes")
    private String observacoes;

    @Column(name = "valor_total", nullable = false)
    @JsonProperty("valor_total")
    private Double valorTotal;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    @JsonProperty("status")
    private StatusPedido status = StatusPedido.PENDENTE;

    public Pedido() {
        this.dataPedido = LocalDateTime.now();
        this.itensPedido = new ArrayList<>();
        this.status = StatusPedido.PENDENTE;
    }

    public void adicionarItem(ItemPedido item) {
        item.setPedido(this); // ⚠️ IMPORTANTE: Setar o pedido no item
        this.itensPedido.add(item);
    }

    @PrePersist
    @PreUpdate
    public void calcularTotal() {
        if (this.itensPedido != null && !this.itensPedido.isEmpty()) {
            this.valorTotal = this.itensPedido.stream()
                .mapToDouble(item -> {
                    if (item.getSubtotal() != null) {
                        return item.getSubtotal();
                    } else if (item.getPrecoUnitario() != null && item.getQuantidade() != null) {
                        return item.getPrecoUnitario() * item.getQuantidade();
                    }
                    return 0.0;
                })
                .sum();
        } else {
            this.valorTotal = 0.0;
        }
    }
}