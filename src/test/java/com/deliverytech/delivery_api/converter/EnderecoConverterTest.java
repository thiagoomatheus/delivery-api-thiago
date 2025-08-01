package com.deliverytech.delivery_api.converter;

import com.deliverytech.delivery_api.converter.EnderecoConverter;
import com.deliverytech.delivery_api.model.Endereco;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class EnderecoConverterTest {

    private EnderecoConverter enderecoConverter = new EnderecoConverter();

    @Test
    public void testConvertToDatabaseColumn() throws Exception {
        // Crie um objeto Endereco para testar
        Endereco endereco = new Endereco("Rua A", "123", "Bairro A", "Cidade A", "Estado A", "12345-678");

        // Converta o objeto Endereco para uma string JSON
        String enderecoJson = enderecoConverter.convertToDatabaseColumn(endereco);

        // Verifique se a string JSON não é nula
        assertNotNull(enderecoJson);

        // Verifique se a string JSON contém os dados corretos
        assertEquals("{\"rua\":\"Rua A\",\"numero\":\"123\",\"bairro\":\"Bairro A\",\"cidade\":\"Cidade A\",\"estado\":\"Estado A\",\"cep\":\"12345-678\"}", enderecoJson);
    }

    @Test
    public void testConvertToEntityAttribute() throws Exception {
        // Crie uma string JSON que representa um objeto Endereco
        String enderecoJson = "{\"rua\":\"Rua A\",\"numero\":\"123\",\"bairro\":\"Bairro A\",\"cidade\":\"Cidade A\",\"estado\":\"Estado A\",\"cep\":\"12345-678\"}";

        // Converta a string JSON para um objeto Endereco
        Endereco endereco = enderecoConverter.convertToEntityAttribute(enderecoJson);

        // Verifique se o objeto Endereco não é nulo
        assertNotNull(endereco);

        // Verifique se o objeto Endereco contém os dados corretos
        assertEquals("Rua A", endereco.getRua());
        assertEquals("123", endereco.getNumero());
        assertEquals("Bairro A", endereco.getBairro());
        assertEquals("Cidade A", endereco.getCidade());
        assertEquals("Estado A", endereco.getEstado());
        assertEquals("12345-678", endereco.getCep());
    }
}