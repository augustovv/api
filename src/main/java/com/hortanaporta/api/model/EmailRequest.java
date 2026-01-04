package com.hortanaporta.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class EmailRequest {
    
    @JsonProperty("destinatario")
    private String destinatario;
    
    @JsonProperty("assunto")
    private String assunto;
    
    @JsonProperty("mensagem")
    private String mensagem;
    
    @JsonProperty("tipo")
    private String tipo; // "TEXTO" ou "HTML"
    
    // Construtores
    public EmailRequest() {}
    
    public EmailRequest(String destinatario, String assunto, String mensagem, String tipo) {
        this.destinatario = destinatario;
        this.assunto = assunto;
        this.mensagem = mensagem;
        this.tipo = tipo;
    }
    
    // Getters e Setters
    public String getDestinatario() {
        return destinatario;
    }
    
    public void setDestinatario(String destinatario) {
        this.destinatario = destinatario;
    }
    
    public String getAssunto() {
        return assunto;
    }
    
    public void setAssunto(String assunto) {
        this.assunto = assunto;
    }
    
    public String getMensagem() {
        return mensagem;
    }
    
    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }
    
    public String getTipo() {
        return tipo;
    }
    
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}