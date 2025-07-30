package com.deliverytech.delivery_api.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.deliverytech.delivery_api.model.Pedido;
import com.deliverytech.delivery_api.model.StatusPedido;
import com.deliverytech.delivery_api.repository.PedidoRepository;
import com.deliverytech.delivery_api.service.impl.PedidoServiceImpl;

@ExtendWith(MockitoExtension.class)
class PedidoServiceTest {

    @Mock
    private PedidoRepository pedidoRepository;

    @InjectMocks
    private PedidoServiceImpl pedidoService;

    private Pedido pedido;
    
    @BeforeEach
    void setUp() {
        pedido = new Pedido(1L, null, null, new BigDecimal(20.00), StatusPedido.EM_PREPARACAO, LocalDateTime.now(), null, null);
    }

    @Test
    @DisplayName("Deve buscar todos os pedidos")
    void deveBuscarTodosOsPedidos() {
        // Arrange
        List<Pedido> pedidos = Arrays.asList(pedido);
        when(pedidoRepository.findAll()).thenReturn(pedidos);

        // Act
        List<Pedido> resultado = pedidoService.buscarTodos();

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(pedidoRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Deve buscar pedido por ID")
    void deveBuscarPedidoPorId() {
        // Arrange
        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedido));

        // Act
        Optional<Pedido> resultado = pedidoService.buscarPorId(1L);

        // Assert
        assertTrue(resultado.isPresent());
        assertEquals(pedido, resultado.get());
        verify(pedidoRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Deve cadastrar pedido")
    void deveCadastrarPedido() {
        // Arrange
        when(pedidoRepository.save(any(Pedido.class))).thenReturn(pedido);

        // Act
        Pedido resultado = pedidoService.criar(pedido);

        // Assert
        assertNotNull(resultado);
        assertEquals(pedido, resultado);
        verify(pedidoRepository, times(1)).save(any(Pedido.class));
    }

    @Test
    @DisplayName("Deve listar pedidos por Id do cliente")
    void deveListarPedidosPorIdDoCliente() {
        // Arrange
        when(pedidoRepository.findByClienteId(any(Long.class))).thenReturn(Arrays.asList(pedido));
        // Act
        List<Pedido> resultado = pedidoService.listarPorCliente(1L);
        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(pedidoRepository, times(1)).findByClienteId(any(Long.class));
    }

    @Test
    @DisplayName("Deve listar pedidos por restaurante")
    void deveListarPedidosPorRestaurante() {
        // Arrange
        when(pedidoRepository.findByRestauranteId(any(Long.class))).thenReturn(Arrays.asList(pedido));
        // Act
        List<Pedido> resultado = pedidoService.listarPorRestaurante(1L);
        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(pedidoRepository, times(1)).findByRestauranteId(any(Long.class));
    }

    @Test
    @DisplayName("Deve atualizar status do pedido")
    void deveAtualizarStatusDoPedido() {
        // Arrange
        when(pedidoRepository.save(any(Pedido.class))).thenReturn(pedido);
        when(pedidoRepository.findById(pedido.getId())).thenReturn(Optional.of(pedido));
        // Act
        assertDoesNotThrow(() -> pedidoService.atualizarStatus(pedido.getId(), StatusPedido.CANCELADO));
        // Assert
        verify(pedidoRepository, times(1)).save(any(Pedido.class));
    }

    @Test
    @DisplayName("Deve cancelar pedido")
    void deveCancelarPedido() {
        // Arrange
        when(pedidoRepository.save(any(Pedido.class))).thenReturn(pedido);
        when(pedidoRepository.findById(pedido.getId())).thenReturn(Optional.of(pedido));
        // Act
        assertDoesNotThrow(() -> pedidoService.cancelar(pedido.getId()));
        // Assert
        verify(pedidoRepository, times(1)).save(any(Pedido.class));
    }
    
}
