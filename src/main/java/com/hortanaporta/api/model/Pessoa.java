package com.hortanaporta.api.model;


import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "tb_pessoa")
@Data
@Inheritance(strategy = InheritanceType.JOINED)
public class Pessoa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CdPessoa")
    @JsonProperty("CdPessoa") // ← ADICIONE ESTA LINHA
    private Long id;

    @Column(name = "NmPessoa")
    @JsonProperty("NmPessoa") // ← ADICIONE ESTA LINHA
    private String nome;

    @Column(name = "CpfPessoa")
    @JsonProperty("CpfPessoa") // ← ADICIONE ESTA LINHA
    private String cpf;

    @Column(name = "EmailPessoa")
    @JsonProperty("EmailPessoa") // ← ADICIONE ESTA LINHA
    private String email;

    @Column(name = "SenhaPessoa")
    @JsonProperty("SenhaPessoa") // ← ADICIONE ESTA LINHA
    private String senha;
}