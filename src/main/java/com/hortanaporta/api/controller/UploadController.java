package com.hortanaporta.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@RestController
@RequestMapping("/api/upload")
@CrossOrigin(origins = "http://localhost:4200")
public class UploadController {

    // Diretório onde as imagens serão salvas
    private static final String UPLOAD_DIR = "src/main/resources/static/assets/imagens/produtos/";
    
    @PostMapping("/imagem-produto")
    public ResponseEntity<String> uploadImagem(@RequestParam("file") MultipartFile file) {
        try {
            // Garante que o diretório existe
            Path uploadPath = Paths.get(UPLOAD_DIR);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            
            // Gera um nome único para o arquivo
            String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
            Path filePath = uploadPath.resolve(fileName);
            
            // Salva o arquivo
            Files.copy(file.getInputStream(), filePath);
            
            // Retorna o caminho relativo para salvar no banco
            String caminhoRelativo = "assets/imagens/produtos/" + fileName;
            return ResponseEntity.ok(caminhoRelativo);
            
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body("Erro ao fazer upload: " + e.getMessage());
        }
    }
    
}//oi