package com.deliverytech.delivery_api.dto.response;

import com.deliverytech.delivery_api.model.Role;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioResponse {

    private Long id;
    private String nome;
    private String email;
    private Role role;
    private Long restauranteId;
}
