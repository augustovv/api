package com.hortanaporta.api.model;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "forma_de_pagamento")
@Data
public class forma_de_pagamento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cd_forma_de_pagamento")
    @JsonProperty("cd_forma_de_pagamento")
    private Long cd_forma_de_pagamento;

    @Column(name = "descricao", nullable = false)
    @JsonProperty("descricao")
    private String descricao;

    @OneToMany(mappedBy = "forma_de_pagamento", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Pedido> pedidos = new ArrayList<>();
}