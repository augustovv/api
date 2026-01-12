package com.hortanaporta.api.services;

import com.hortanaporta.api.model.ItemPedido;
import com.hortanaporta.api.model.Pedido;
import com.hortanaporta.api.model.Produto;
import com.hortanaporta.api.repository.PedidoRepository;
import com.hortanaporta.api.repository.ProdutoRepository;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;
    
    @Autowired
    private ProdutoRepository produtoRepository;

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
                item.setPedido(pedido);
                // Busca preço do produto se não estiver definido
                if (item.getPrecoUnitario() == null && item.getProduto() != null && item.getProduto().getCdProduto() != null) {
                    Produto produto = produtoRepository.findById(item.getProduto().getCdProduto()).orElse(null);
                    if (produto != null) {
                        item.setPrecoUnitario(produto.getPreco());
                    }
                }
                item.calcularSubtotal();
            }
        }
        
        pedido.calcularTotal();
        return pedidoRepository.save(pedido);
    }

    @Transactional
    public Pedido atualizar(Long id, Pedido pedidoAtualizado) {
        Optional<Pedido> pedidoExistente = pedidoRepository.findById(id);
        if (pedidoExistente.isPresent()) {
            Pedido pedido = pedidoExistente.get();
            
            // Atualiza campos básicos
            pedido.setStatus(pedidoAtualizado.getStatus());
            pedido.setObservacoes(pedidoAtualizado.getObservacoes());
            
            // Atualiza relacionamentos diretos se fornecidos
            if (pedidoAtualizado.getPessoa() != null) {
                pedido.setPessoa(pedidoAtualizado.getPessoa());
            }
            if (pedidoAtualizado.getEnderecoEntrega() != null) {
                pedido.setEnderecoEntrega(pedidoAtualizado.getEnderecoEntrega());
            }
            if (pedidoAtualizado.getForma_de_pagamento() != null) {
                pedido.setForma_de_pagamento(pedidoAtualizado.getForma_de_pagamento());
            }
            
            // Atualiza os itens do pedido buscando preços do banco
            atualizarItensPedidoComPrecos(pedido, pedidoAtualizado.getItensPedido());
            
            pedido.calcularTotal();
            return pedidoRepository.save(pedido);
        } else {
            return null;
        }
    }

    private void atualizarItensPedidoComPrecos(Pedido pedidoExistente, List<ItemPedido> novosItens) {
        // Se não houver novos itens, limpa a lista
        if (novosItens == null) {
            pedidoExistente.getItensPedido().clear();
            return;
        }
        
        List<ItemPedido> itensAtualizados = new ArrayList<>();
        
        for (ItemPedido novoItem : novosItens) {
            // Para itens existentes (com cdItemPedido)
            if (novoItem.getCdItemPedido() != null) {
                // Busca o item existente no pedido
                Optional<ItemPedido> itemExistenteOpt = pedidoExistente.getItensPedido().stream()
                    .filter(item -> item.getCdItemPedido().equals(novoItem.getCdItemPedido()))
                    .findFirst();
                
                if (itemExistenteOpt.isPresent()) {
                    ItemPedido itemExistente = itemExistenteOpt.get();
                    
                    // Atualiza apenas a quantidade (mantém preço original)
                    if (novoItem.getQuantidade() != null) {
                        itemExistente.setQuantidade(novoItem.getQuantidade());
                    }
                    
                  
                    
                    itemExistente.calcularSubtotal();
                    itensAtualizados.add(itemExistente);
                }
            } 
            // Para novos itens (sem cdItemPedido mas com cd_produto)
            else if (novoItem.getProduto() != null && novoItem.getProduto().getCdProduto() != null) {
                ItemPedido item = new ItemPedido();
                item.setPedido(pedidoExistente);
                item.setProduto(novoItem.getProduto());
                item.setQuantidade(novoItem.getQuantidade());
                
                // Busca preço do produto no banco
                Produto produto = produtoRepository.findById(novoItem.getProduto().getCdProduto()).orElse(null);
                if (produto != null) {
                    item.setPrecoUnitario(produto.getPreco());
                } else {
                    throw new RuntimeException("Produto não encontrado: " + novoItem.getProduto().getCdProduto());
                }
                
                item.calcularSubtotal();
                itensAtualizados.add(item);
            }
        }
        
        // Atualiza a lista de itens do pedido
        pedidoExistente.getItensPedido().clear();
        pedidoExistente.getItensPedido().addAll(itensAtualizados);
    }
    
    @Transactional
    public Pedido atualizarSimplificado(Long id, Pedido pedidoAtualizado) {
        Optional<Pedido> pedidoExistente = pedidoRepository.findById(id);
        if (pedidoExistente.isPresent()) {
            Pedido pedido = pedidoExistente.get();
            
            // Atualiza campos básicos
            if (pedidoAtualizado.getStatus() != null) {
                pedido.setStatus(pedidoAtualizado.getStatus());
            }
            if (pedidoAtualizado.getObservacoes() != null) {
                pedido.setObservacoes(pedidoAtualizado.getObservacoes());
            }
            
            // Atualiza itens de forma simplificada
            atualizarItensSimplificado(pedido, pedidoAtualizado.getItensPedido());
            
            pedido.calcularTotal();
            return pedidoRepository.save(pedido);
        } else {
            return null;
        }
    }
    
    private void atualizarItensSimplificado(Pedido pedido, List<ItemPedido> itensSimplificados) {
        if (itensSimplificados == null) {
            pedido.getItensPedido().clear();
            return;
        }
        
        List<ItemPedido> itensAtualizados = new ArrayList<>();
        
        for (ItemPedido itemSimplificado : itensSimplificados) {
            // Item existente - atualiza apenas quantidade
            if (itemSimplificado.getCdItemPedido() != null) {
                Optional<ItemPedido> itemExistenteOpt = pedido.getItensPedido().stream()
                    .filter(item -> item.getCdItemPedido().equals(itemSimplificado.getCdItemPedido()))
                    .findFirst();
                
                if (itemExistenteOpt.isPresent()) {
                    ItemPedido itemExistente = itemExistenteOpt.get();
                    if (itemSimplificado.getQuantidade() != null) {
                        itemExistente.setQuantidade(itemSimplificado.getQuantidade());
                        itemExistente.calcularSubtotal();
                    }
                    itensAtualizados.add(itemExistente);
                }
            }
            // Novo item - precisa do produto e quantidade
            else if (itemSimplificado.getProduto() != null && 
                     itemSimplificado.getProduto().getCdProduto() != null &&
                     itemSimplificado.getQuantidade() != null) {
                
                ItemPedido novoItem = new ItemPedido();
                novoItem.setPedido(pedido);
                novoItem.setProduto(itemSimplificado.getProduto());
                novoItem.setQuantidade(itemSimplificado.getQuantidade());
                
                // Busca preço do banco
                Produto produto = produtoRepository.findById(itemSimplificado.getProduto().getCdProduto())
                    .orElseThrow(() -> new RuntimeException("Produto não encontrado: " + 
                        itemSimplificado.getProduto().getCdProduto()));
                
                novoItem.setPrecoUnitario(produto.getPreco());
                novoItem.calcularSubtotal();
                itensAtualizados.add(novoItem);
            }
        }
        
        pedido.getItensPedido().clear();
        pedido.getItensPedido().addAll(itensAtualizados);
    }

    public boolean deletar(Long id) {
        if (pedidoRepository.existsById(id)) {
            pedidoRepository.deleteById(id);
            return true;
        }
        return false;
    }
}