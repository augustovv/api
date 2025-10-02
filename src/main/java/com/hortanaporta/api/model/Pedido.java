package com.hortanaporta.api.model;

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
    private Long id;

    @Column(name = "data_pedido", nullable = false)
    @JsonProperty("data_pedido")
    private Data data_pedido;

    @Column(name = "observacao", nullable = false)
    @JsonProperty("observacao")
    private String observacao;


    @Column(name = "cd_entrega", nullable = false)
    @JsonProperty("cd_entrega")
    private String cd_entrega;

    @Column(name = "cd_produto", nullable = false)
    @JsonProperty("cd_produto")
    private String cd_produto;



   
}