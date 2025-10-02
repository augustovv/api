package com.hortanaporta.api.services;

import com.hortanaporta.api.model.Pedido;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PedidoService {

    private List<Pedido> pedidos = new ArrayList<>();
    private Long proximoId = 1L;

    public List<Pedido> listarTodos() {
        return pedidos;
    }

    public Pedido buscarPorId(Long id) {
        return pedidos.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public Pedido criar(Pedido pedido) {
        pedido.setId(proximoId++);
        pedidos.add(pedido);
        return pedido;
    }

    public Pedido atualizar(Long id, Pedido pedidoAtualizado) {
        Optional<Pedido> pedidoExistente = pedidos.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst();

        if (pedidoExistente.isPresent()) {
            Pedido pedido = pedidoExistente.get();
            // Atualize os campos necessários
            pedido.setObservacao(pedidoAtualizado.getObservacao());
            pedido.setQuantidade(pedidoAtualizado.getQuantidade());
            // Adicione outros campos conforme necessário
            return pedido;
        }
        return null;
    }

    public boolean deletar(Long id) {
        return pedidos.removeIf(p -> p.getId().equals(id));
    }
}
