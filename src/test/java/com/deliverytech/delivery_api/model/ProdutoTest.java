package com.deliverytech.delivery_api.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

import com.deliverytech.delivery_api.model.Produto;
import com.deliverytech.delivery_api.model.Restaurante;

class ProdutoTest {

    @Test
    void deveCriarProduto() {
        Produto produto = new Produto(1L, "Produto 1", "Categoria 1", "Descrição 1", new BigDecimal(10.00), true, new Restaurante());
        assertNotNull(produto);

        assertEquals(1L, produto.getId());
        assertEquals("Produto 1", produto.getNome());
        assertEquals(new BigDecimal(10.00), produto.getPreco());
        assertEquals("Categoria 1", produto.getCategoria());
        assertEquals("Descrição 1", produto.getDescricao());
        assertEquals(true, produto.getDisponivel());
    }

    @Test
    void deveTestarSetters() {
        Produto produto = new Produto();
        produto.setId(1L);
        produto.setNome("Produto 1");
        produto.setPreco(new BigDecimal(10.00));
        produto.setCategoria("Categoria 1");
        produto.setDescricao("Descrição 1");
        produto.setDisponivel(true);
        produto.setRestaurante(new Restaurante());
        assertNotNull(produto);
        assertEquals(1L, produto.getId());
        assertEquals("Produto 1", produto.getNome());
        assertEquals(new BigDecimal(10.00), produto.getPreco());
        assertEquals("Categoria 1", produto.getCategoria());
        assertEquals("Descrição 1", produto.getDescricao());
        assertEquals(true, produto.getDisponivel());
        assertEquals(new Restaurante(), produto.getRestaurante());
    }
    
}
