package com.hortanaporta.api.services;

import com.hortanaporta.api.model.Categoria;
import com.hortanaporta.api.repository.CategoriaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;

    public CategoriaService(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    @Transactional(readOnly = true)
    public List<Categoria> listarTodos() {
        return categoriaRepository.findAll();
    }



    @Transactional
    public Categoria salvar(Categoria categoria) {
        // Validação adicional pode ser adicionada aqui
        return categoriaRepository.save(categoria);
    }

    @Transactional
    public void excluir(Long id) {
       
        categoriaRepository.deleteById(id);
    }

    @Transactional
    public Categoria atualizar(Long id, Categoria categoriaAtualizada) {
        return categoriaRepository.findById(id)
                .map(categoria -> {
                    categoria.setNome(categoriaAtualizada.getNome());
                    // Atualize outros campos conforme necessário
                    return categoriaRepository.save(categoria);
                })
                .orElseThrow();
    }

    public Categoria buscarPorId(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'buscarPorId'");
    }
}