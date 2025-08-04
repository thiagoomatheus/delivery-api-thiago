package com.deliverytech.delivery_api.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

import com.deliverytech.delivery_api.model.Restaurante;

class RestauranteTest {

    @Test
    void deveCriarRestaurante () {
        Restaurante restaurante = new Restaurante(1L, "Restaurante A", "Pizzaria", "123456789", new BigDecimal(10.00), 30, true, null);

        assertNotNull(restaurante);
        assertEquals(1L, restaurante.getId());
        assertEquals("Restaurante A", restaurante.getNome());
        assertEquals("Pizzaria", restaurante.getCategoria());
        assertEquals("123456789", restaurante.getTelefone());
        assertEquals(new BigDecimal(10.00), restaurante.getTaxaEntrega());
        assertEquals(30, restaurante.getTempoEntregaMinutos());
        assertEquals(true, restaurante.getAtivo());
        assertEquals(null, restaurante.getProdutos());
    }

    @Test
    void deveTestarSetters () {
        Restaurante restaurante = new Restaurante();
        restaurante.setId(1L);
        restaurante.setNome("Restaurante A");
        restaurante.setCategoria("Pizzaria");
        restaurante.setTelefone("123456789");
        restaurante.setTaxaEntrega(new BigDecimal(10.00));
        restaurante.setTempoEntregaMinutos(30);
        restaurante.setAtivo(true);
        restaurante.setProdutos(null);
        assertNotNull(restaurante);
        assertEquals(1L, restaurante.getId());
        assertEquals("Restaurante A", restaurante.getNome());
        assertEquals("Pizzaria", restaurante.getCategoria());
        assertEquals("123456789", restaurante.getTelefone());
        assertEquals(new BigDecimal(10.00), restaurante.getTaxaEntrega());
        assertEquals(30, restaurante.getTempoEntregaMinutos());
        assertEquals(true, restaurante.getAtivo());
        assertEquals(null, restaurante.getProdutos());
    }
    
}
