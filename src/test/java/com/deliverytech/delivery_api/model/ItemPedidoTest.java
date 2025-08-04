package com.deliverytech.delivery_api.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

import com.deliverytech.delivery_api.model.ItemPedido;
import com.deliverytech.delivery_api.model.Pedido;
import com.deliverytech.delivery_api.model.Produto;

class ItemPedidoTest {

    @Test
    void deveCriarItemPedido() {

        Pedido pedido = new Pedido();
        Produto produto = new Produto();

        ItemPedido itemPedido = new ItemPedido(1L, pedido, produto, 2, new BigDecimal(10.00));

        assertNotNull(itemPedido);
        assertEquals(1L, itemPedido.getId());
        assertEquals(pedido, itemPedido.getPedido());
        assertEquals(produto, itemPedido.getProduto());
        assertEquals(2, itemPedido.getQuantidade());
        assertEquals(new BigDecimal(10.00), itemPedido.getPrecoUnitario());
    }

    @Test
    void deveTestarSetters() {

        Pedido pedido = new Pedido();
        Produto produto = new Produto();

        ItemPedido itemPedido = new ItemPedido();
        itemPedido.setId(1L);
        itemPedido.setPedido(pedido);
        itemPedido.setProduto(produto);
        itemPedido.setQuantidade(2);
        itemPedido.setPrecoUnitario(new BigDecimal(10.00));
        assertNotNull(itemPedido);
        assertEquals(1L, itemPedido.getId());
        assertEquals(pedido, itemPedido.getPedido());
        assertEquals(produto, itemPedido.getProduto());
        assertEquals(2, itemPedido.getQuantidade());
        assertEquals(new BigDecimal(10.00), itemPedido.getPrecoUnitario());
    }
    
}
