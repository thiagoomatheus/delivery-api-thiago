package com.deliverytech.delivery_api.converter;

import com.deliverytech.delivery_api.converter.EnderecoConverter;
import com.deliverytech.delivery_api.model.Endereco;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class EnderecoConverterTest {

    private EnderecoConverter enderecoConverter = new EnderecoConverter();

    @Test
    public void deveConverterEnderecoParaJson() throws Exception {
        Endereco endereco = new Endereco("Rua A", "123", "Bairro A", "Cidade A", "Estado A", "12345-678");

        String enderecoJson = enderecoConverter.convertToDatabaseColumn(endereco);

        assertNotNull(enderecoJson);

        assertEquals("{\"rua\":\"Rua A\",\"numero\":\"123\",\"bairro\":\"Bairro A\",\"cidade\":\"Cidade A\",\"estado\":\"Estado A\",\"cep\":\"12345-678\"}", enderecoJson);
    }

    @Test
    public void deveConverterEnderecoParaEntidade() throws Exception {
        String enderecoJson = "{\"rua\":\"Rua A\",\"numero\":\"123\",\"bairro\":\"Bairro A\",\"cidade\":\"Cidade A\",\"estado\":\"Estado A\",\"cep\":\"12345-678\"}";

        Endereco endereco = enderecoConverter.convertToEntityAttribute(enderecoJson);

        assertNotNull(endereco);

        assertEquals("Rua A", endereco.getRua());
        assertEquals("123", endereco.getNumero());
        assertEquals("Bairro A", endereco.getBairro());
        assertEquals("Cidade A", endereco.getCidade());
        assertEquals("Estado A", endereco.getEstado());
        assertEquals("12345-678", endereco.getCep());
    }

    @Test
    public void deveReceberNullAoReceberEnderecoJsonNulo() throws Exception {
        String enderecoJson = null;

        Endereco endereco = enderecoConverter.convertToEntityAttribute(enderecoJson);

        assertEquals(null, endereco);
    }
}