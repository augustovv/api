package com.hortanaporta.api.services;

import com.hortanaporta.api.model.ItemPedido;
import com.hortanaporta.api.model.Pedido;
import com.hortanaporta.api.repository.PedidoRepository;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    public List<Pedido> listarTodos() {
        return pedidoRepository.findAll();
    }

    public Pedido buscarPorId(Long id) {
        return pedidoRepository.findById(id).orElse(null);
    }

   
    @Transactional
    public Pedido criar(Pedido pedido) {
        // Garantir que os itens tenham referência ao pedido
        if (pedido.getItensPedido() != null) {
            for (ItemPedido item : pedido.getItensPedido()) {
                item.setPedido(pedido); // ⚠️ IMPORTANTE
                item.calcularSubtotal(); // Calcular subtotal
            }
        }
        
        pedido.calcularTotal(); // Calcular total
        return pedidoRepository.save(pedido);
    }

    public Pedido atualizar(Long id, Pedido pedidoAtualizado) {
        Optional<Pedido> pedidoExistente = pedidoRepository.findById(id);
        if (pedidoExistente.isPresent()) {
            Pedido pedido = pedidoExistente.get();
            // Atualize os campos necessários do pedido
            //pedido.setObservacao(pedidoAtualizado.getObservacao());
            //pedido.setQuantidade(pedidoAtualizado.getQuantidade());
            // Adicione outros campos conforme necessário
            return pedidoRepository.save(pedido);
        }
        return null;
    }

    public boolean deletar(Long id) {
        if (pedidoRepository.existsById(id)) {
            pedidoRepository.deleteById(id);
            return true;
        }
        return false;
    }
}