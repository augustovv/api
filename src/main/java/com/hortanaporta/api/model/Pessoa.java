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
    @JsonProperty("CdPessoa")
    private Long id;

    @Column(name = "NmPessoa")
    @JsonProperty("NmPessoa")
    private String nome;

    @Column(name = "CpfPessoa")
    @JsonProperty("CpfPessoa")
    private String cpf;

    @Column(name = "EmailPessoa")
    @JsonProperty("EmailPessoa")
    private String email;

    @Column(name = "SenhaPessoa")
    @JsonProperty("SenhaPessoa")
    private String senha;

    // NOVO CAMPO - Role (papel)
    @Column(name = "RolePessoa")
    @JsonProperty("RolePessoa")
    private String role = "CLIENTE"; // valor padrão
}