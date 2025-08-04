package com.deliverytech.delivery_api.model;

import java.time.LocalDateTime;
import java.math.BigDecimal;
import com.deliverytech.delivery_api.model.Entrega;
import com.deliverytech.delivery_api.model.Endereco;
import com.deliverytech.delivery_api.model.Entregador;
import com.deliverytech.delivery_api.model.Pedido;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class EntregaTest {

    @Test
    void deveCriarEntrega() {

        LocalDateTime horarioEstimadoEntrega = LocalDateTime.now();
        LocalDateTime horarioRealizadoEntrega = LocalDateTime.now();

        Pedido pedido = new Pedido();
        Entregador entregador = new Entregador();

        Entrega entrega = new Entrega(1L, new Endereco(), "ENTREGUE", horarioEstimadoEntrega, horarioRealizadoEntrega, BigDecimal.valueOf(10.0), pedido, entregador);
        assertNotNull(entrega);

        assertEquals(entrega.getId(), 1L);
        assertEquals(entrega.getEnderecoEntrega(), new Endereco());
        assertEquals(entrega.getStatus(), "ENTREGUE");
        assertEquals(entrega.getHorarioEstimadoEntrega(), horarioEstimadoEntrega);
        assertEquals(entrega.getHorarioRealizadoEntrega(), horarioRealizadoEntrega);
        assertEquals(entrega.getTaxaEntrega(), BigDecimal.valueOf(10.0));
        assertEquals(entrega.getPedido(), pedido);
        assertEquals(entrega.getEntregador(), entregador);
    }

    @Test
    void deveTestarSetters() {

        LocalDateTime horarioEstimadoEntrega = LocalDateTime.now();
        LocalDateTime horarioRealizadoEntrega = LocalDateTime.now();

        Pedido pedido = new Pedido();
        Entregador entregador = new Entregador();

        Entrega entrega = new Entrega();
        entrega.setId(1L);
        entrega.setEnderecoEntrega(new Endereco());
        entrega.setStatus("ENTREGUE");
        entrega.setHorarioEstimadoEntrega(horarioEstimadoEntrega);
        entrega.setHorarioRealizadoEntrega(horarioRealizadoEntrega);
        entrega.setTaxaEntrega(BigDecimal.valueOf(10.0));
        entrega.setPedido(pedido);
        entrega.setEntregador(entregador);
        assertNotNull(entrega);
        assertEquals(1L, entrega.getId());
        assertEquals(new Endereco(), entrega.getEnderecoEntrega());
        assertEquals("ENTREGUE", entrega.getStatus());
        assertEquals(horarioEstimadoEntrega, entrega.getHorarioEstimadoEntrega());
        assertEquals(horarioRealizadoEntrega, entrega.getHorarioRealizadoEntrega());
        assertEquals(BigDecimal.valueOf(10.0), entrega.getTaxaEntrega());
        assertEquals(pedido, entrega.getPedido());
        assertEquals(entregador, entrega.getEntregador());
    }
    
}
