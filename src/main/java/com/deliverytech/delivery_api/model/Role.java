package com.deliverytech.delivery_api.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Roles de usuário no sistema")
public enum Role {
    @Schema(description = "Cliente do sistema")
    CLIENTE,

    @Schema(description = "Restaurante associado ao sistema")
    RESTAURANTE,

    @Schema(description = "Administrador do sistema")
    ADMIN,
    
    @Schema(description = "Entregador associado ao sistema")
    ENTREGADOR
}