package com.deliverytech.delivery_api.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Status do pedido no sistema")
public enum StatusPedido {
    @Schema(description = "Pedido foi criado")
    CRIADO,

    @Schema(description = "Pedido foi confirmado")
    CONFIRMADO,

    @Schema(description = "Pedido em preparação")
    EM_PREPARACAO,

    @Schema(description = "Pedido saiu para entrega")
    ENVIADO,

    @Schema(description = "Pedido entregue ao cliente")
    ENTREGUE,

    @Schema(description = "Pedido foi cancelado")
    CANCELADO
}