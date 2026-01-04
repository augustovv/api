package com.hortanaporta.api.controller;

import com.hortanaporta.api.model.EmailRequest;
import com.hortanaporta.api.services.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/email")
@CrossOrigin("*")
public class EmailController {
    
    @Autowired
    private EmailService emailService;
    
    /**
     * Endpoint para enviar email simples
     * POST /api/email/enviar
     */
    @PostMapping("/enviar")
    public ResponseEntity<?> enviarEmail(@RequestBody EmailRequest emailRequest) {
        try {
            System.out.println("📧 Recebendo solicitação de email:");
            System.out.println("Destinatário: " + emailRequest.getDestinatario());
            System.out.println("Assunto: " + emailRequest.getAssunto());
            System.out.println("Tipo: " + emailRequest.getTipo());
            
            if (emailRequest.getDestinatario() == null || emailRequest.getDestinatario().isEmpty()) {
                return ResponseEntity.badRequest().body("Destinatário é obrigatório");
            }
            
            if (emailRequest.getAssunto() == null || emailRequest.getAssunto().isEmpty()) {
                return ResponseEntity.badRequest().body("Assunto é obrigatório");
            }
            
            boolean sucesso = emailService.enviarEmail(emailRequest);
            
            Map<String, Object> resposta = new HashMap<>();
            if (sucesso) {
                resposta.put("status", "sucesso");
                resposta.put("mensagem", "Email enviado com sucesso");
                resposta.put("destinatario", emailRequest.getDestinatario());
                return ResponseEntity.ok(resposta);
            } else {
                resposta.put("status", "erro");
                resposta.put("mensagem", "Falha ao enviar email");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(resposta);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, String> erro = new HashMap<>();
            erro.put("status", "erro");
            erro.put("mensagem", "Erro interno: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(erro);
        }
    }
    
    /**
     * Endpoint para enviar confirmação de pedido
     * POST /api/email/confirmacao-pedido
     */
    @PostMapping("/confirmacao-pedido")
    public ResponseEntity<?> enviarConfirmacaoPedido(@RequestBody Map<String, String> dados) {
        try {
            String email = dados.get("email");
            String numeroPedido = dados.get("numeroPedido");
            String detalhes = dados.get("detalhes");
            
            System.out.println("🛒 Enviando confirmação de pedido:");
            System.out.println("Email: " + email);
            System.out.println("Pedido: " + numeroPedido);
            
            if (email == null || numeroPedido == null) {
                return ResponseEntity.badRequest().body("Email e número do pedido são obrigatórios");
            }
            
            boolean sucesso = emailService.enviarConfirmacaoPedido(email, numeroPedido, 
                detalhes != null ? detalhes : "Seu pedido foi recebido e está sendo processado.");
            
            Map<String, Object> resposta = new HashMap<>();
            if (sucesso) {
                resposta.put("status", "sucesso");
                resposta.put("mensagem", "Confirmação de pedido enviada");
                resposta.put("numeroPedido", numeroPedido);
                return ResponseEntity.ok(resposta);
            } else {
                resposta.put("status", "erro");
                resposta.put("mensagem", "Falha ao enviar confirmação");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(resposta);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Erro: " + e.getMessage());
        }
    }
    
    /**
     * Endpoint para enviar recuperação de senha
     * POST /api/email/recuperacao-senha
     */
    @PostMapping("/recuperacao-senha")
    public ResponseEntity<?> enviarRecuperacaoSenha(@RequestBody Map<String, String> dados) {
        try {
            String email = dados.get("email");
            String token = dados.get("token");
            
            System.out.println("🔐 Enviando recuperação de senha para: " + email);
            
            if (email == null || token == null) {
                return ResponseEntity.badRequest().body("Email e token são obrigatórios");
            }
            
            boolean sucesso = emailService.enviarRecuperacaoSenha(email, token);
            
            Map<String, Object> resposta = new HashMap<>();
            if (sucesso) {
                resposta.put("status", "sucesso");
                resposta.put("mensagem", "Email de recuperação enviado");
                return ResponseEntity.ok(resposta);
            } else {
                resposta.put("status", "erro");
                resposta.put("mensagem", "Falha ao enviar email de recuperação");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(resposta);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Erro: " + e.getMessage());
        }
    }
    
    /**
     * Endpoint de teste
     * GET /api/email/test
     */
    @GetMapping("/test")
    public ResponseEntity<?> test() {
        Map<String, Object> resposta = new HashMap<>();
        resposta.put("status", "ok");
        resposta.put("mensagem", "API de Email funcionando");
        resposta.put("timestamp", System.currentTimeMillis());
        resposta.put("endpoints", new String[] {
            "POST /api/email/enviar",
            "POST /api/email/confirmacao-pedido", 
            "POST /api/email/recuperacao-senha",
            "GET /api/email/test"
        });
        return ResponseEntity.ok(resposta);
    }
}