package com.hortanaporta.api.controller;

import com.hortanaporta.api.model.Endereco;
import com.hortanaporta.api.services.EnderecoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/enderecos")
public class EnderecoController {

    @Autowired
    private EnderecoService enderecoService;

    // Buscar endereço por CEP (ViaCEP) - PERMITIDO SEM AUTENTICAÇÃO
    @GetMapping("/cep/{cep}")
    public ResponseEntity<?> buscarPorCep(@PathVariable String cep) {
        System.out.println("=== DEBUG ENDERECO CONTROLLER ===");
        System.out.println("Buscando CEP: " + cep);
        
        try {
            Endereco endereco = enderecoService.buscarPorCep(cep);
            
            if (endereco != null) {
                System.out.println("✅ CEP encontrado: " + endereco.getLogradouro() + ", " + endereco.getBairro());
                return ResponseEntity.ok(endereco);
            } else {
                System.out.println("❌ CEP não encontrado: " + cep);
                return ResponseEntity.badRequest().body(Map.of("error", "CEP não encontrado"));
            }
        } catch (Exception e) {
            System.out.println("💥 ERRO ao buscar CEP: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(Map.of("error", "Erro interno ao buscar CEP"));
        }
    }

    // Endpoint simples para teste
    @GetMapping("/test")
    public ResponseEntity<String> test() {
        System.out.println("✅ Endpoint de teste funcionando!");
        return ResponseEntity.ok("Endereço Controller funcionando!");
    }

    // Salvar endereço no banco - PRECISA DE AUTENTICAÇÃO
    @PostMapping
    public ResponseEntity<Endereco> salvarEndereco(@RequestBody Endereco endereco) {
        System.out.println("Salvando endereço para pessoa: " + endereco.getCdPessoa());
        Endereco enderecoSalvo = enderecoService.salvarEndereco(endereco);
        return ResponseEntity.ok(enderecoSalvo);
    }

    // Buscar endereços por pessoa - PRECISA DE AUTENTICAÇÃO
    @GetMapping("/pessoa/{cdPessoa}")
    public ResponseEntity<List<Endereco>> buscarEnderecosPorPessoa(@PathVariable Long cdPessoa) {
        List<Endereco> enderecos = enderecoService.buscarEnderecosPorPessoa(cdPessoa);
        return ResponseEntity.ok(enderecos);
    }

    // Buscar endereço principal da pessoa
    @GetMapping("/pessoa/{cdPessoa}/principal")
    public ResponseEntity<Endereco> buscarEnderecoPrincipal(@PathVariable Long cdPessoa) {
        return enderecoService.buscarEnderecoPrincipal(cdPessoa)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Definir endereço como principal
    @PutMapping("/{cdEndereco}/principal")
    public ResponseEntity<Endereco> definirEnderecoPrincipal(
            @PathVariable Long cdEndereco, 
            @RequestBody Map<String, Long> request) {
        
        Long cdPessoa = request.get("cdPessoa");
        Endereco endereco = enderecoService.definirEnderecoPrincipal(cdEndereco, cdPessoa);
        
        if (endereco != null) {
            return ResponseEntity.ok(endereco);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Deletar endereço
    @DeleteMapping("/{cdEndereco}")
    public ResponseEntity<Void> deletarEndereco(@PathVariable Long cdEndereco) {
        boolean removido = enderecoService.deletarEndereco(cdEndereco);
        if (removido) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}