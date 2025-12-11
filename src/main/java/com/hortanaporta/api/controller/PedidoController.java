package com.hortanaporta.api.controller;
import com.hortanaporta.api.services.PedidoService;
import jakarta.validation.Valid;
import com.hortanaporta.api.model.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import com.hortanaporta.api.services.PessoaService;
import com.hortanaporta.api.services.EnderecoService;
import com.hortanaporta.api.services.FormaDePagamentoService;
import com.hortanaporta.api.services.ProdutoService;
@RestController
@RequestMapping("/api/pedidos")
@CrossOrigin("*")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;
    private final PessoaService pessoaService;
    private final EnderecoService enderecoService;
    private final ProdutoService produtoService;
    private final FormaDePagamentoService formaDePagamentoService;

    public PedidoController(PedidoService pedidoService, PessoaService pessoaService, EnderecoService enderecoService, ProdutoService produtoService, FormaDePagamentoService formaDePagamentoService) {
        this.pedidoService = pedidoService;
        this.pessoaService = pessoaService;
        this.enderecoService = enderecoService;
        this.produtoService = produtoService;
        this.formaDePagamentoService = formaDePagamentoService;
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
            return ResponseEntity.ok(pedidoAtualizado);
        } else {
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

    @PostMapping
    public ResponseEntity<?> criarPedido(@RequestBody Pedido pedido) {
    try {
        // 1. VALIDAÇÕES INICIAIS
        if (pedido.getPessoa() == null || pedido.getPessoa().getId() == null) {
            return ResponseEntity.badRequest().body("Pessoa é obrigatória");
        }
        
        if (pedido.getEnderecoEntrega() == null || pedido.getEnderecoEntrega().getCd_endereco() == null) {
            return ResponseEntity.badRequest().body("Endereço de entrega é obrigatório");
        }

        if (pedido.getForma_de_pagamento() == null) {
           
            return ResponseEntity.badRequest().body("Forma de pagamento está nula" + pedido.getForma_de_pagamento() + " " + pedido.getForma_de_pagamento().getCd_forma_de_pagamento());
        }
        if (pedido.getForma_de_pagamento().getCd_forma_de_pagamento() == null) {
            return ResponseEntity.badRequest().body("o código da Forma de pagamento está nulo" + pedido.getForma_de_pagamento() + " " + pedido.getForma_de_pagamento().getCd_forma_de_pagamento());
        }

        // 2. BUSCAR E COMPLETAR DADOS DA PESSOA
        Pessoa pessoaDoPedido = pessoaService.buscarPorId(pedido.getPessoa().getId());
        if (pessoaDoPedido == null) {
            return ResponseEntity.badRequest().body("Pessoa não encontrada");
        }
        pedido.setPessoa(pessoaDoPedido);

        // 3. BUSCAR E COMPLETAR DADOS DO ENDEREÇO
        Endereco enderecoDoPedido = enderecoService.buscarEnderecosPorPessoa(pedido.getPessoa().getId())
            .stream()
            .filter(endereco -> endereco.getCd_endereco().equals(pedido.getEnderecoEntrega().getCd_endereco()))
            .findFirst()
            .orElse(null);
            
        if (enderecoDoPedido == null) {
            return ResponseEntity.badRequest().body("Endereço não encontrado");
        }
        pedido.setEnderecoEntrega(enderecoDoPedido);

        // 4. BUSCAR E COMPLETAR FORMA DE PAGAMENTO (NOVO)
        forma_de_pagamento formaPagamento = formaDePagamentoService.buscarPorId(pedido.getForma_de_pagamento().getCd_forma_de_pagamento());
        if (formaPagamento == null) {
            return ResponseEntity.badRequest().body("Forma de pagamento não encontrada");
        }
        pedido.setForma_de_pagamento(formaPagamento); // ⬅️ IMPORTANTE: Usar entidade gerenciada

        // 5. VALIDAR E COMPLETAR DADOS DOS ITENS
        for (ItemPedido item : pedido.getItensPedido()) {
            // Validar quantidade
            if (item.getQuantidade() == null || item.getQuantidade() <= 0) {
                return ResponseEntity.badRequest().body("Quantidade deve ser maior que zero");
            }
            
            // Buscar produto completo
            Produto produtoDoItem = produtoService.buscarPorId(item.getProduto().getId());
            if (produtoDoItem == null) {
                return ResponseEntity.badRequest().body("Produto não encontrado: ID " + item.getProduto().getId());
            }
            
            item.setProduto(produtoDoItem);
            item.setPedido(pedido); // Estabelecer relação bidirecional
        }

        // 6. SALVAR O PEDIDO
        Pedido pedidoSalvo = pedidoService.criar(pedido);

        return ResponseEntity.status(HttpStatus.CREATED).body(pedidoSalvo);
        
    } catch (Exception e) {
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body("Erro ao criar pedido: " + e.getMessage());
    }
}
}

   
