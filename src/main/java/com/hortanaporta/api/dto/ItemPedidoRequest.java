package com.hortanaporta.api.dto;

import java.math.BigDecimal;

public class ItemPedidoRequest {
    private Long cdProduto;
    private Integer quantidade;
    private BigDecimal vlUnitario;
    private BigDecimal vlTotal;

    // Construtores
    public ItemPedidoRequest() {
    }

    public ItemPedidoRequest(Long cdProduto, Integer quantidade, BigDecimal vlUnitario, BigDecimal vlTotal) {
        this.cdProduto = cdProduto;
        this.quantidade = quantidade;
        this.vlUnitario = vlUnitario;
        this.vlTotal = vlTotal;
    }

    // Getters e Setters
    public Long getCdProduto() {
        return cdProduto;
    }

    public void setCdProduto(Long cdProduto) {
        this.cdProduto = cdProduto;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public BigDecimal getVlUnitario() {
        return vlUnitario;
    }

    public void setVlUnitario(BigDecimal vlUnitario) {
        this.vlUnitario = vlUnitario;
    }

    public BigDecimal getVlTotal() {
        return vlTotal;
    }

    public void setVlTotal(BigDecimal vlTotal) {
        this.vlTotal = vlTotal;
    }
}