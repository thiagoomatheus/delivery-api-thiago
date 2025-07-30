package com.deliverytech.delivery_api.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.web.servlet.MockMvc;

import com.deliverytech.delivery_api.dto.request.ItemPedidoRequest;
import com.deliverytech.delivery_api.dto.request.PedidoRequest;
import com.deliverytech.delivery_api.model.Cliente;
import com.deliverytech.delivery_api.model.Endereco;
import com.deliverytech.delivery_api.model.ItemPedido;
import com.deliverytech.delivery_api.model.Pedido;
import com.deliverytech.delivery_api.model.Produto;
import com.deliverytech.delivery_api.model.Restaurante;
import com.deliverytech.delivery_api.model.StatusPedido;
import com.deliverytech.delivery_api.security.JwtUtil;
import com.deliverytech.delivery_api.service.impl.ClienteServiceImpl;
import com.deliverytech.delivery_api.service.impl.PedidoServiceImpl;
import com.deliverytech.delivery_api.service.impl.ProdutoServiceImpl;
import com.deliverytech.delivery_api.service.impl.RestauranteServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(
  controllers = PedidoController.class,
  excludeAutoConfiguration = {SecurityAutoConfiguration.class}
)
class PedidoControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PedidoServiceImpl pedidoService;

    @MockBean
    private ClienteServiceImpl clienteService;

    @MockBean
    private RestauranteServiceImpl restauranteService;

    @MockBean
    private ProdutoServiceImpl produtoService;

    @MockBean
    private JwtUtil jwtUtil;

    @MockBean
    private UserDetailsService userDetailsService;

    @Autowired
    private ObjectMapper objectMapper;

    private Pedido pedido;
    private Cliente cliente;
    private Restaurante restaurante;
    private Produto produto;
    private ItemPedido itemPedido;
    private Endereco endereco;

    @BeforeEach
    void setUp() {
        cliente = new Cliente(1L, "Cliente Teste", "xZx2a@example.com", true, LocalDateTime.now(), null);

        restaurante = new Restaurante(1L, "Restaurante A", "Pizzaria", "123456789", new BigDecimal(10.00), 30, true, null);

        produto = new Produto(1L, "Pizza Margherita", "Pizza", "Pizza com molho de tomate e mussarela", new BigDecimal(25.90), true, restaurante);

        itemPedido = new ItemPedido(1L, null, produto, 2, new BigDecimal(10.00));

        endereco = new Endereco("Rua A", "123", "Bairro A", "Cidade A", "Estado A", "12345-678");

        pedido = new Pedido(1L, cliente, restaurante, new BigDecimal(20.00), StatusPedido.EM_PREPARACAO, LocalDateTime.now(), Arrays.asList(itemPedido), endereco);

        itemPedido.setPedido(pedido);
    }

    @Test
    @DisplayName("Deve criar um novo pedido com sucesso")
    void criarPedido() throws Exception {
        when(clienteService.buscarPorId(anyLong())).thenReturn(Optional.of(cliente));
        when(restauranteService.buscarPorId(anyLong())).thenReturn(Optional.of(restaurante));
        when(produtoService.buscarPorId(anyLong())).thenReturn(Optional.of(produto));
        when(pedidoService.criar(any(Pedido.class))).thenReturn(pedido);

        ItemPedidoRequest itemPedidoRequest = new ItemPedidoRequest(
            produto.getId(),
            itemPedido.getQuantidade()
        );

        // Crie o objeto PedidoRequest usando sua classe DTO real
        PedidoRequest pedidoRequest = new PedidoRequest(
            cliente.getId(),
            restaurante.getId(),
            endereco, // Use o objeto Endereco diretamente conforme seu DTO
            Arrays.asList(itemPedidoRequest)
        );

        mockMvc.perform(post("/api/pedidos")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(pedidoRequest)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value(pedido.getId()));
        verify(pedidoService, times(1)).criar(any(Pedido.class));
    }

    @Test
    @DisplayName("Deve buscar um pedido por ID")
    void buscarPedidoPorId() throws Exception {
        when(pedidoService.buscarPorId(anyLong())).thenReturn(Optional.of(pedido));

        mockMvc.perform(get("/api/pedidos/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(pedido.getId()));
        verify(pedidoService, times(1)).buscarPorId(1L);
    }

    @Test
    @DisplayName("Deve alterar o status de um pedido")
    void alterarStatusPedido() throws Exception {
        when(pedidoService.buscarPorId(anyLong())).thenReturn(Optional.of(pedido));
        when(pedidoService.atualizarStatus(anyLong(), any(StatusPedido.class))).thenReturn(pedido);

        mockMvc.perform(put("/api/pedidos/{pedidoId}/status", 1L)
            .param("status", StatusPedido.ENTREGUE.name()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value(StatusPedido.ENTREGUE.name()));
        verify(pedidoService, times(1)).atualizarStatus(1L, StatusPedido.ENTREGUE);
    }
}
