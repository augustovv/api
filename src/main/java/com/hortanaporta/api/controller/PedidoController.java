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

@RestController
@RequestMapping("/api/pedidos")
@CrossOrigin("*")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;
    private final PessoaService pessoaService;
    private final EnderecoService enderecoService;

    public PedidoController(PedidoService pedidoService, PessoaService pessoaService, EnderecoService enderecoService) {
        this.pedidoService = pedidoService;
        this.pessoaService = pessoaService;
        this.enderecoService = enderecoService;
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

            Pedido pedidoSalvo = pedidoService.criar(pedido);
            return ResponseEntity.status(HttpStatus.CREATED).body(pedidoSalvo);
            
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Erro ao criar pedido: " + e.getMessage());
        }
    }
}

