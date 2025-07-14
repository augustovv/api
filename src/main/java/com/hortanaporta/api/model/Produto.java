package com.hortanaporta.api.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Entity
@Table(name = "tb_produto")
@Data
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CdProduto")
    private Long id;

    @Column(name = "NmProduto", nullable = false)
    private String nome;

    @Column(nullable = false)
    private Double preco;

    private String unidade;

    @Column(name = "dataValidade")
    private LocalDate dataValidade;
    
 @ManyToOne
    @JoinColumn(name = "categoria_id") // Nome da coluna no banco
    private Categoria categoria; // Relacionamento Many-to-One
   

    @Column(name = "ativoOuNao")
    private Boolean ativo;

    @Column(name = "caminhoImagem")
    private String caminhoImagem;
}