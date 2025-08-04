package com.deliverytech.delivery_api.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class EnderecoTest {

    @Test
    void deveCriarEnderecoComBuilder() {
        Endereco endereco = Endereco.builder()
                .rua("Rua A")
                .numero("123")
                .bairro("Bairro A")
                .cidade("Cidade A")
                .estado("Estado A")
                .cep("12345-678")
                .build();

        assertNotNull(endereco);
        assertEquals("Rua A", endereco.getRua());
        assertEquals("123", endereco.getNumero());
        assertEquals("Bairro A", endereco.getBairro());
        assertEquals("Cidade A", endereco.getCidade());
        assertEquals("Estado A", endereco.getEstado());
        assertEquals("12345-678", endereco.getCep());
    }

    @Test
    void deveTestarSetters() {
        Endereco endereco = new Endereco();
        endereco.setRua("Rua A");
        endereco.setNumero("123");
        endereco.setBairro("Bairro A");
        endereco.setCidade("Cidade A");
        endereco.setEstado("Estado A");
        endereco.setCep("12345-678");
        assertNotNull(endereco);
        assertEquals("Rua A", endereco.getRua());
        assertEquals("123", endereco.getNumero());
        assertEquals("Bairro A", endereco.getBairro());
        assertEquals("Cidade A", endereco.getCidade());
        assertEquals("Estado A", endereco.getEstado());
        assertEquals("12345-678", endereco.getCep());
    }

    @Test
    void deveCriarEnderecoComConstrutor() {
        Endereco endereco = new Endereco("Rua A", "123", "Bairro A", "Cidade A", "Estado A", "12345-678");

        assertNotNull(endereco);
        assertEquals("Rua A", endereco.getRua());
        assertEquals("123", endereco.getNumero());
        assertEquals("Bairro A", endereco.getBairro());
        assertEquals("Cidade A", endereco.getCidade());
        assertEquals("Estado A", endereco.getEstado());
        assertEquals("12345-678", endereco.getCep());
    }
}