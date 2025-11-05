package com.hortanaporta.api.model;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "tb_pessoa") // ✅ CORRETO
@Data
@Inheritance(strategy = InheritanceType.JOINED)
public class Pessoa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cd_pessoa") // ✅ CORRETO
    @JsonProperty("cd_pessoa")
    private Long id;

    @Column(name = "nm_pessoa") // ✅ CORRIGIDO: era "NmPessoa"
    @JsonProperty("nm_pessoa")
    private String nome;

    @Column(name = "cpf_pessoa") // ✅ CORRIGIDO: era "CpfPessoa"  
    @JsonProperty("cpf_pessoa")
    private String cpf;

    @Column(name = "email_pessoa") // ✅ CORRIGIDO: era "EmailPessoa"
    @JsonProperty("email_pessoa")
    private String email;

    @Column(name = "senha_pessoa") // ✅ CORRIGIDO: era "SenhaPessoa"
    @JsonProperty("senha_pessoa")
    private String senha;

    // ✅ CORRETO - Esta coluna existe no banco!
    @Column(name = "RolePessoa", nullable = false, length = 20)
    @JsonProperty("RolePessoa")
    private String role = "CLIENTE"; // valor padrão

    @OneToMany(mappedBy = "pessoa", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Pedido> pedidos;
}