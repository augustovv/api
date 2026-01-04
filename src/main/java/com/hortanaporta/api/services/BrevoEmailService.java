package com.hortanaporta.api.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import java.util.*;

@Service
public class BrevoEmailService {
    
    @Value("${brevo.api.key}")
    private String apiKey;
    
    @Value("${brevo.sender.email}")
    private String senderEmail;
    
    @Value("${brevo.sender.name}")
    private String senderName;
    
    public boolean enviarEmail(String destinatario, String assunto, String mensagemHtml) {
        try {
            System.out.println("🔐 Usando API Key: " + apiKey.substring(0, 15) + "...");
            System.out.println("📨 Remetente: " + senderEmail);
            
            RestTemplate restTemplate = new RestTemplate();
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("api-key", apiKey);
            headers.set("accept", "application/json");
            
            // Construir o request CORRETO para Brevo
            Map<String, Object> request = new HashMap<>();
            
            // Sender (DEVE ser o email verificado)
            Map<String, String> sender = new HashMap<>();
            sender.put("email", senderEmail);
            sender.put("name", senderName);
            request.put("sender", sender);
            
            // To (destinatário)
            List<Map<String, String>> toList = new ArrayList<>();
            Map<String, String> to = new HashMap<>();
            to.put("email", destinatario);
            toList.add(to);
            request.put("to", toList);
            
            // Subject e conteúdo
            request.put("subject", assunto);
            request.put("htmlContent", mensagemHtml);
            
            // Text content alternativo (opcional mas recomendado)
            String textContent = mensagemHtml.replaceAll("<[^>]*>", "");
            request.put("textContent", textContent);
            
            // Fazer a requisição
            String url = "https://api.brevo.com/v3/smtp/email";
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(request, headers);
            
            System.out.println("📤 Enviando para Brevo API...");
            ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);
            
            System.out.println("📥 Resposta Brevo: " + response.getStatusCode());
            
            if (response.getStatusCode().is2xxSuccessful()) {
                System.out.println("✅ Email enviado com sucesso via Brevo API!");
                return true;
            } else {
                System.err.println("❌ Erro Brevo: " + response.getBody());
                return false;
            }
            
        } catch (Exception e) {
            System.err.println("💥 ERRO: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}