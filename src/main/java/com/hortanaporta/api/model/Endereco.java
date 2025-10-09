package com.hortanaporta.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "tb_endereco")
@Data
public class Endereco {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cd_endereco")
    @JsonProperty("cd_endereco")
    private Long id;

    @Column(name = "cep", nullable = false, length = 9)
    @JsonProperty("cep")
    private String cep;

    @Column(name = "logradouro", nullable = false)
    @JsonProperty("logradouro")
    private String logradouro;

    @Column(name = "numero")
    @JsonProperty("numero")
    private String numero;

    @Column(name = "complemento")
    @JsonProperty("complemento")
    private String complemento;

    @Column(name = "bairro", nullable = false)
    @JsonProperty("bairro")
    private String bairro;

    @Column(name = "cidade", nullable = false)
    @JsonProperty("cidade")
    private String cidade;

    @Column(name = "estado", nullable = false, length = 2)
    @JsonProperty("estado")
    private String estado;

    @Column(name = "cd_pessoa")
    @JsonProperty("cd_pessoa")
    private Long cdPessoa; // Relacionamento com a pessoa

    @Column(name = "endereco_principal")
    @JsonProperty("endereco_principal")
    private Boolean enderecoPrincipal = false;

    // Construtor para facilitar criação via API
    public Endereco() {}

    public Endereco(String cep, String logradouro, String bairro, String cidade, String estado) {
        this.cep = cep;
        this.logradouro = logradouro;
        this.bairro = bairro;
        this.cidade = cidade;
        this.estado = estado;
    }
}