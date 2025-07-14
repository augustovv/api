package com.hortanaporta.api.model;

import java.util.List;

import jakarta.persistence.*;
import lombok.Data;


@Entity
@Table(name = "tb_categoria")
public class Categoria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long CdCategoria;
    private String NmCategoria;


    // Outros campos...

    @OneToMany(mappedBy = "categoria")
    private List<Produto> produtos;

    // Getters e Setters
}