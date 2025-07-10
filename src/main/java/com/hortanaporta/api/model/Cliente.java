package com.hortanaporta.api.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "TbCliente")
@Data
public class Cliente {

    @Id
    @Column(name = "CdPessoa")
    private Long id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "CdPessoa")
    private Pessoa pessoa;
}