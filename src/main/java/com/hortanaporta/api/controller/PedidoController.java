package com.hortanaporta.api.controller;

import com.hortanaporta.api.services.PedidoService;
import com.hortanaporta.api.model.*;
import com.hortanaporta.api.services.PessoaService;
import com.hortanaporta.api.services.EnderecoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/pedidos")
@CrossOrigin("*")
public class PedidoController {

    private final PedidoService pedidoService;
    private final PessoaService pessoaService;
    private final EnderecoService enderecoService;

    // REMOVA o @Autowired de cima e use apenas o construtor
    public PedidoController(PedidoService pedidoService, 
                           PessoaService pessoaService, 
                           EnderecoService enderecoService) {
        this.pedidoService = pedidoService;
        this.pessoaService = pessoaService;
        this.enderecoService = enderecoService;
    }

    @PostMapping
    public ResponseEntity<?> criarPedido(@RequestBody Pedido pedido) {
        try {
            // **ADICIONE ESTA VALIDAÇÃO PARA DEBUG**
            System.out.println("=== PEDIDO RECEBIDO ===");
            System.out.println("Pessoa ID: " + (pedido.getPessoa() != null ? pedido.getPessoa().getId() : "null"));
            System.out.println("Endereço ID: " + (pedido.getEnderecoEntrega() != null ? pedido.getEnderecoEntrega().getCd_endereco() : "null"));
            
            if (pedido.getItensPedido() != null) {
                System.out.println("Itens do pedido:");
                for (ItemPedido item : pedido.getItensPedido()) {
                    System.out.println("  Produto ID: " + (item.getProduto() != null ? item.getProduto().getId() : "null"));
                    System.out.println("  Preço unitário: " + item.getPrecoUnitario());
                    System.out.println("  Quantidade: " + item.getQuantidade());
                    
                    // **CRÍTICO: Verifica se o preço está em centavos**
                    if (item.getPrecoUnitario() > 1000) {
                        System.out.println("  ⚠️ PREÇO EM CENTAVOS DETECTADO: " + item.getPrecoUnitario() + 
                                         " (R$ " + (item.getPrecoUnitario() / 100) + ")");
                    }
                }
            }

            // Validar dados obrigatórios
            if (pedido.getPessoa() == null || pedido.getPessoa().getId() == null) {
                return ResponseEntity.badRequest().body("Pessoa é obrigatória");
            }
            
            if (pedido.getEnderecoEntrega() == null || pedido.getEnderecoEntrega().getCd_endereco() == null) {
                return ResponseEntity.badRequest().body("Endereço de entrega é obrigatório");
            }

            // **CRÍTICO: CORRIGE OS PREÇOS ANTES DE SALVAR**
            if (pedido.getItensPedido() != null) {
                for (ItemPedido item : pedido.getItensPedido()) {
                    // Se o preço veio em centavos do frontend, converte para reais
                    if (item.getPrecoUnitario() != null && item.getPrecoUnitario() > 1000) {
                        System.out.println("🔄 Convertendo preço: " + item.getPrecoUnitario() + " -> " + (item.getPrecoUnitario() / 100));
                        item.setPrecoUnitario(item.getPrecoUnitario() / 100);
                    }
                }
            }

            Pessoa pessoaDoPedido = pessoaService.buscarPorId(pedido.getPessoa().getId());

            if (pessoaDoPedido == null) {
                return ResponseEntity.badRequest().body("Pessoa não encontrada");
            }

            // Copia dados da pessoa
            pedido.getPessoa().setNome(pessoaDoPedido.getNome());
            pedido.getPessoa().setCpf(pessoaDoPedido.getCpf());
            pedido.getPessoa().setEmail(pessoaDoPedido.getEmail());

            Pedido pedidoSalvo = pedidoService.criar(pedido);
            
            System.out.println("✅ Pedido criado com sucesso: #" + pedidoSalvo.getCd_pedido());
            return ResponseEntity.status(HttpStatus.CREATED).body(pedidoSalvo);
            
        } catch (Exception e) {
            System.err.println("❌ ERRO AO CRIAR PEDIDO:");
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Erro ao criar pedido: " + e.getMessage());
        }
    }


    @GetMapping
    public ResponseEntity<List<Pedido>> listarPedidos() {   
        List<Pedido> pedidos = pedidoService.listarTodos();
        return ResponseEntity.ok(pedidos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pedido> obterPedidoPorId(@PathVariable Long id) {
        Pedido pedido = pedidoService.buscarPorId(id);
        if (pedido != null) {
            return ResponseEntity.ok(pedido);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Pedido> atualizarPedido(@PathVariable Long id, @RequestBody Pedido pedido) {
        Pedido pedidoAtualizado = pedidoService.atualizar(id, pedido);
        if (pedidoAtualizado != null) {
            System.out.println("Pedido com ID " + id + " atualizado com sucesso.");
            return ResponseEntity.ok(pedidoAtualizado);
        } else {
            System.out.println("Pedido com ID " + id + " não encontrado para atualização.");
            return ResponseEntity.notFound().build();
        }
    }   

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarPedido(@PathVariable Long id) {  
        boolean deletado = pedidoService.deletar(id);
        if (deletado) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }   
}

