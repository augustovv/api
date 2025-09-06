package com.hortanaporta.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "tb_produto")
@Data
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cd_produto")
    @JsonProperty("cd_produto")
    private Long id;

    @Column(name = "ativo_ou_nao")
    @JsonProperty("ativo_ou_nao")
    private Boolean ativo;

    @Column(name = "caminho_imagem")
    @JsonProperty("caminho_imagem")
    private String caminhoImagem;

    @Column(name = "data_validade", nullable = true)
    @JsonProperty("data_validade")
    private String dataValidade;

    @Column(name = "nm_produto", nullable = false)
    @JsonProperty("nm_produto")
    private String nome;

    @Column(name = "preco", nullable = false)
    @JsonProperty("preco")
    private Long preco;

    @Column(name = "unidade")
    @JsonProperty("unidade")
    private String unidade;

    @Column(name = "categoria")
    @JsonProperty("categoria")
    private String categoria;

     @Column(name = "observacoes")
    @JsonProperty("observacoes")
    private String observacoes;
}