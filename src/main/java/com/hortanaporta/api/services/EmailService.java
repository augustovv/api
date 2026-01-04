package com.hortanaporta.api.services;

import com.hortanaporta.api.model.EmailRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    
    @Autowired
    private BrevoEmailService brevoEmailService;
    
    public boolean enviarEmail(EmailRequest emailRequest) {
        try {
            String mensagemHtml;
            
            if ("HTML".equalsIgnoreCase(emailRequest.getTipo())) {
                mensagemHtml = emailRequest.getMensagem();
            } else {
                // Converter texto para HTML simples
                mensagemHtml = "<html><body><pre style=\"font-family: Arial, sans-serif;\">"
                    + emailRequest.getMensagem().replace("\n", "<br>")
                    + "</pre></body></html>";
            }
            
            return brevoEmailService.enviarEmail(
                emailRequest.getDestinatario(),
                emailRequest.getAssunto(),
                mensagemHtml
            );
            
        } catch (Exception e) {
            System.err.println("Erro no EmailService: " + e.getMessage());
            return false;
        }
    }
    
    // Mantenha os outros métodos, mas atualize para usar brevoEmailService
    public boolean enviarConfirmacaoPedido(String emailCliente, String numeroPedido, String detalhes) {
        String assunto = "Confirmação de Pedido #" + numeroPedido;
        String html = criarHtmlConfirmacaoPedido(numeroPedido, detalhes);
        
        return brevoEmailService.enviarEmail(emailCliente, assunto, html);
    }
    
    public boolean enviarRecuperacaoSenha(String emailUsuario, String token) {
        String assunto = "Recuperação de Senha";
        String html = criarHtmlRecuperacaoSenha(token);
        
        return brevoEmailService.enviarEmail(emailUsuario, assunto, html);
    }
    
    private String criarHtmlConfirmacaoPedido(String numeroPedido, String detalhes) {
        return "<h1>Pedido Confirmado #" + numeroPedido + "</h1>"
            + "<p>" + detalhes + "</p>"
            + "<p>Obrigado por sua compra!</p>";
    }
    
    private String criarHtmlRecuperacaoSenha(String token) {
        return "<h1>Recuperação de Senha</h1>"
            + "<p>Seu token: <strong>" + token + "</strong></p>"
            + "<p>Use este token para redefinir sua senha.</p>";
    }
}