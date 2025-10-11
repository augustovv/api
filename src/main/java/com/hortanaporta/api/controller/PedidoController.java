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
import com.hortanaporta.api.services.ProdutoService;
import com.hortanaporta.api.model.Pessoa;
@RestController
@RequestMapping("/api/pedidos")
@CrossOrigin("*")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;
    private final PessoaService pessoaService;
    private final EnderecoService enderecoService;
    private final ProdutoService produtoService;

    public PedidoController(PedidoService pedidoService, PessoaService pessoaService, EnderecoService enderecoService, ProdutoService produtoService) {
        this.pedidoService = pedidoService;
        this.pessoaService = pessoaService;
        this.enderecoService = enderecoService;
        this.produtoService = produtoService;
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
            // Validar dados obrigatórios
            if (pedido.getPessoa() == null || pedido.getPessoa().getId() == null) {
                return ResponseEntity.badRequest().body("Pessoa é obrigatória");
            }
            
            if (pedido.getEnderecoEntrega() == null || pedido.getEnderecoEntrega().getCd_endereco() == null) {
                return ResponseEntity.badRequest().body("Endereço de entrega é obrigatório");
            }

            Pessoa pessoaDoPedido = pessoaService.buscarPorId(pedido.getPessoa().getId());
            pedido.getPessoa().setNome(pessoaDoPedido.getNome());;
            pedido.getPessoa().setCpf(pessoaDoPedido.getCpf());
            pedido.getPessoa().setEmail(pessoaDoPedido.getEmail());

            Endereco enderecoDoPedido = enderecoService.buscarEnderecosPorPessoa(pedido.getPessoa().getId())
                .stream()
                .filter(endereco -> endereco.getCd_endereco().equals(pedido.getEnderecoEntrega().getCd_endereco()))
                .findFirst()
                .orElse(null);
            pedido.getEnderecoEntrega().setLogradouro(enderecoDoPedido.getLogradouro());
            pedido.getEnderecoEntrega().setNumero(enderecoDoPedido.getNumero());
            pedido.getEnderecoEntrega().setComplemento(enderecoDoPedido.getComplemento());
            pedido.getEnderecoEntrega().setBairro(enderecoDoPedido.getBairro());
            pedido.getEnderecoEntrega().setCidade(enderecoDoPedido.getCidade());
            pedido.getEnderecoEntrega().setEstado(enderecoDoPedido.getEstado());
            pedido.getEnderecoEntrega().setCep(enderecoDoPedido.getCep());


            for(var i = 0; i < pedido.getItensPedido().size(); i++) {
                Produto produtoDoItem = this.produtoService.buscarPorId(pedido.getItensPedido().get(i).getProduto().getId());
                pedido.getItensPedido().get(i).getProduto().setNome(produtoDoItem.getNome());
                pedido.getItensPedido().get(i).getProduto().setObservacoes(produtoDoItem.getObservacoes());
                pedido.getItensPedido().get(i).getProduto().setPreco(produtoDoItem.getPreco());
                pedido.getItensPedido().get(i).getProduto().setCaminhoImagem(produtoDoItem.getCaminhoImagem());
                pedido.getItensPedido().get(i).getProduto().setCategoria(produtoDoItem.getCategoria());
                pedido.getItensPedido().get(i).getProduto().setAtivo(produtoDoItem.getAtivo());
                pedido.getItensPedido().get(i).getProduto().setDataValidade(produtoDoItem.getDataValidade());


                
                if (pedido.getItensPedido().get(i).getQuantidade() == null || pedido.getItensPedido().get(i).getQuantidade() <= 0) {
                    return ResponseEntity.badRequest().body("Quantidade deve ser maior que zero para o produto: " + produtoDoItem.getNome());
                }
            }

            

            Pedido pedidoSalvo = pedidoService.criar(pedido);
            return ResponseEntity.status(HttpStatus.CREATED).body(pedidoSalvo);
            
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Erro ao criar pedido: " + e.getMessage());
        }
    }
}

