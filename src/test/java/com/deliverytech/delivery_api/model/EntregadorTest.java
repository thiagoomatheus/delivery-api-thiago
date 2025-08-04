package com.deliverytech.delivery_api.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import com.deliverytech.delivery_api.model.Entregador;


class EntregadorTest {

    @Test
    void deveCriarEntregador() {

        LocalDateTime dataCriacao = LocalDateTime.now();

        Entregador entregador = new Entregador(1L, "Joaquim", "7VZyG@example.com", "11999999999", true, dataCriacao, null);
        assertNotNull(entregador);

        assertEquals(entregador.getId(), 1L);
        assertEquals(entregador.getNome(), "Joaquim");
        assertEquals(entregador.getEmail(), "7VZyG@example.com");
        assertEquals(entregador.getTelefone(), "11999999999");
        assertTrue(entregador.getAtivo());
        assertEquals(entregador.getDataCriacao(), dataCriacao);
    }

    @Test
    void deveTestarSetters() {

        LocalDateTime dataCriacao = LocalDateTime.now();

        Entregador entregador = new Entregador();
        entregador.setId(1L);
        entregador.setNome("Joaquim");
        entregador.setEmail("7VZyG@example.com");
        entregador.setTelefone("11999999999");
        entregador.setAtivo(true);
        entregador.setDataCriacao(dataCriacao);
        assertNotNull(entregador);
        assertEquals(1L, entregador.getId());
        assertEquals("Joaquim", entregador.getNome());
        assertEquals("7VZyG@example.com", entregador.getEmail());
        assertEquals("11999999999", entregador.getTelefone());
        assertTrue(entregador.getAtivo());
        assertEquals(dataCriacao, entregador.getDataCriacao());
    }
    
}
