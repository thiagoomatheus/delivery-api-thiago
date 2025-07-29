package com.deliverytech.delivery_api.service.impl;

import com.deliverytech.delivery_api.model.Produto;
import com.deliverytech.delivery_api.repository.ProdutoRepository;
import com.deliverytech.delivery_api.service.ProdutoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProdutoServiceImpl implements ProdutoService {

    private final ProdutoRepository produtoRepository;

    @Override
    public Produto cadastrar(Produto produto) {
        if (produto.getNome() == null || produto.getNome().isEmpty()) {
            throw new IllegalArgumentException("O nome do produto não pode ser vazio");
        }
        if (produto.getPreco() == null || produto.getPreco().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("O preço do produto deve ser maior que zero");
        }
        if (produto.getCategoria() == null || produto.getCategoria().isEmpty()) {
            throw new IllegalArgumentException("A categoria do produto não pode ser vazia");
        }
        if (produto.getDescricao() == null || produto.getDescricao().isEmpty()) {
            throw new IllegalArgumentException("A descrição do produto não pode ser vazia");
        }
        if (produto.getRestaurante() == null) {
            throw new IllegalArgumentException("O produto deve estar associado a um restaurante");
        }
        if (produto.getRestaurante().getId() == null) {
            throw new IllegalArgumentException("O restaurante associado ao produto deve ter um ID válido");
        }
        return produtoRepository.save(produto);
    }

    @Override
    public Optional<Produto> buscarPorId(Long id) {
        return produtoRepository.findById(id);
    }

    @Override
    public List<Produto> buscarPorRestaurante(Long restauranteId) {
        return produtoRepository.findByRestauranteId(restauranteId);
    }

    @Override
    public Produto atualizar(Long id, Produto atualizado) {
        return produtoRepository.findById(id)
            .map(p -> {
                p.setNome(atualizado.getNome());
                p.setDescricao(atualizado.getDescricao());
                p.setCategoria(atualizado.getCategoria());
                p.setPreco(atualizado.getPreco());
                return produtoRepository.save(p);
            }).orElseThrow(() -> new RuntimeException("Produto não encontrado"));
    }

    @Override
    public void alterarDisponibilidade(Long id, boolean disponivel) {
        produtoRepository.findById(id).ifPresent(p -> {
            p.setDisponivel(disponivel);
            produtoRepository.save(p);
        });
    }

    @Override
    public List<Produto> buscarTodos() {
        return produtoRepository.findAll();
    }
}
