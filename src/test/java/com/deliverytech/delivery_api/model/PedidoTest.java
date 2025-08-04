package com.deliverytech.delivery_api.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.deliverytech.delivery_api.model.Pedido;
import com.deliverytech.delivery_api.model.StatusPedido;

class PedidoTest {

    @Test
    void deveCriarPedido() {

        LocalDateTime dataPedido = LocalDateTime.now();

        Pedido pedido = new Pedido(1L, null, null, new BigDecimal(20.00), StatusPedido.EM_PREPARACAO, dataPedido, null, null);

        assertNotNull(pedido);

        assertEquals(1L, pedido.getId());
        assertEquals(null, pedido.getCliente());
        assertEquals(null, pedido.getRestaurante());
        assertEquals(new BigDecimal(20.00), pedido.getTotal());
        assertEquals(StatusPedido.EM_PREPARACAO, pedido.getStatus());
        assertEquals(dataPedido, pedido.getDataPedido());
        assertEquals(null, pedido.getItens());
        assertEquals(null, pedido.getEnderecoEntrega());

    }

    @Test
    void deveTestarSetters() {

        LocalDateTime dataPedido = LocalDateTime.now();

        Pedido pedido = new Pedido();
        pedido.setId(1L);
        pedido.setCliente(null);
        pedido.setRestaurante(null);
        pedido.setTotal(new BigDecimal(20.00));
        pedido.setStatus(StatusPedido.EM_PREPARACAO);
        pedido.setDataPedido(dataPedido);
        pedido.setItens(null);
        pedido.setEnderecoEntrega(null);
        assertNotNull(pedido);
        assertEquals(1L, pedido.getId());
        assertEquals(null, pedido.getCliente());
        assertEquals(null, pedido.getRestaurante());
        assertEquals(new BigDecimal(20.00), pedido.getTotal());
        assertEquals(StatusPedido.EM_PREPARACAO, pedido.getStatus());
        assertEquals(dataPedido, pedido.getDataPedido());
        assertEquals(null, pedido.getItens());
        assertEquals(null, pedido.getEnderecoEntrega());
        
    }
}