package com.hortanaporta.api.dto;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class PedidoRequest {
    private Long cdCliente;
    private Long cdEndereco;
    private LocalDateTime dtPedido;
    private String dsStatus;
    private BigDecimal vlTotal;
    private List<ItemPedidoRequest> itens;

    // Construtores
    public PedidoRequest() {
    }

    public PedidoRequest(Long cdCliente, Long cdEndereco, LocalDateTime dtPedido, String dsStatus, BigDecimal vlTotal) {
        this.cdCliente = cdCliente;
        this.cdEndereco = cdEndereco;
        this.dtPedido = dtPedido;
        this.dsStatus = dsStatus;
        this.vlTotal = vlTotal;
    }

    // Getters e Setters
    public Long getCdCliente() {
        return cdCliente;
    }

    public void setCdCliente(Long cdCliente) {
        this.cdCliente = cdCliente;
    }

    public Long getCdEndereco() {
        return cdEndereco;
    }

    public void setCdEndereco(Long cdEndereco) {
        this.cdEndereco = cdEndereco;
    }

    public LocalDateTime getDtPedido() {
        return dtPedido;
    }

    public void setDtPedido(LocalDateTime dtPedido) {
        this.dtPedido = dtPedido;
    }

    public String getDsStatus() {
        return dsStatus;
    }

    public void setDsStatus(String dsStatus) {
        this.dsStatus = dsStatus;
    }

    public BigDecimal getVlTotal() {
        return vlTotal;
    }

    public void setVlTotal(BigDecimal vlTotal) {
        this.vlTotal = vlTotal;
    }

    public List<ItemPedidoRequest> getItens() {
        return itens;
    }

    public void setItens(List<ItemPedidoRequest> itens) {
        this.itens = itens;
    }
}