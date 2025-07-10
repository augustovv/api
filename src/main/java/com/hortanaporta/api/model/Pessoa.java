package com.hortanaporta.api.model;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "TbPessoa")
@Data
@Inheritance(strategy = InheritanceType.JOINED)
public class Pessoa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CdPessoa")
    private Long id;

    @Column(name = "NmPessoa", nullable = false)
    private String nome;

    @Column(name = "CpfPessoa", unique = true)
    private String cpf;

    @Column(name = "EmailPessoa", unique = true)
    private String email;

    @Column(name = "SenhaPessoa")
    private String senha;
}