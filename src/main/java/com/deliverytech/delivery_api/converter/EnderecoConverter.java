package com.deliverytech.delivery_api.converter;

import com.deliverytech.delivery_api.model.Endereco;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class EnderecoConverter implements AttributeConverter<Endereco, String> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(Endereco endereco) {
        try {
            return objectMapper.writeValueAsString(endereco);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Erro ao converter Endereco para JSON", e);
        }
    }

    @Override
    public Endereco convertToEntityAttribute(String enderecoJson) {
        if (enderecoJson == null || enderecoJson.trim().isEmpty()) {
            return null;
        }
        try {
            return objectMapper.readValue(enderecoJson, Endereco.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Erro ao converter JSON para Endereco", e);
        }
    }
}
