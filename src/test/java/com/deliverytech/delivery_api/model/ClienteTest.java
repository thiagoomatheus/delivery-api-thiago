package com.deliverytech.delivery_api.model;

import java.time.LocalDateTime;
import com.deliverytech.delivery_api.model.Cliente;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ClienteTest {

    @Test
    void deveCriarCliente() {
        Cliente cliente = new Cliente(1L, "João", "joao@email.com", true, LocalDateTime.now(), null);
        assertNotNull(cliente);
        assertEquals(1L, cliente.getId());
        assertEquals("João", cliente.getNome());
        assertEquals("joao@email.com", cliente.getEmail());
        assertTrue(cliente.getAtivo());
    }

    @Test
    void deveTestarSetters() {
        Cliente cliente = new Cliente();
        cliente.setId(1L);
        cliente.setNome("João");
        cliente.setEmail("joao@email.com");
        cliente.setAtivo(true);
        assertNotNull(cliente);
        assertEquals(1L, cliente.getId());
        assertEquals("João", cliente.getNome());
        assertEquals("joao@email.com", cliente.getEmail());
        assertTrue(cliente.getAtivo());
    }

    @Test
    void deveCriarClienteComBuilder() {
        Cliente cliente = Cliente.builder()
            .id(1L)
            .nome("João")
            .email("joao@email.com")
            .ativo(true)
            .dataCriacao(LocalDateTime.now())
            .build();
        assertNotNull(cliente);
        assertEquals(1L, cliente.getId());
        assertEquals("João", cliente.getNome());
        assertEquals("joao@email.com", cliente.getEmail());
        assertTrue(cliente.getAtivo());
    }
}