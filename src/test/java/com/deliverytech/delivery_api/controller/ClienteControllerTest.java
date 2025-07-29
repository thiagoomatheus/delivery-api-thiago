package com.deliverytech.delivery_api.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

import com.deliverytech.delivery_api.model.Cliente;
import com.deliverytech.delivery_api.security.JwtUtil;
import com.deliverytech.delivery_api.service.impl.ClienteServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(
  controllers = ClienteController.class,
  excludeAutoConfiguration = {SecurityAutoConfiguration.class}
)
class ClienteControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  @MockBean
  private ClienteServiceImpl clienteService;

  @Autowired
  private ObjectMapper objectMapper;
  
  private Cliente cliente;

  @MockBean
  private JwtUtil jwtUtil;

  @MockBean
  private UserDetailsService userDetailsService;

  @BeforeEach
  void setUp() {
    cliente = new Cliente(null, "João", "joao@email.com", true, LocalDateTime.now(), null);
  }

  @Test
  @DisplayName("Deve buscar todos os clientes ativos")
  void deveBuscarTodosOsClientes() throws Exception {
    // Arrange
    when(clienteService.listarAtivos()).thenReturn(Arrays.asList(cliente));

    // Act & Assert
    mockMvc.perform(get("/api/clientes"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$").isArray())
      .andExpect(jsonPath("$[0].nome").value("João"))
      .andExpect(jsonPath("$[0].email").value("joao@email.com"));

    verify(clienteService, times(1)).listarAtivos();
  }

  @Test
  @DisplayName("Deve buscar cliente por ID")
  void deveBuscarClientePorId() throws Exception {
    // Arrange
    when(clienteService.buscarPorId(1L)).thenReturn(Optional.of(cliente));

    // Act & Assert
    mockMvc.perform(get("/api/clientes/1"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.nome").value("João"))
      .andExpect(jsonPath("$.email").value("joao@email.com"));

    verify(clienteService, times(1)).buscarPorId(1L);
  }

  @Test
  @DisplayName("Deve retornar 404 para cliente não encontrado")
  void deveRetornar404ParaClienteNaoEncontrado() throws Exception {
    // Arrange
    when(clienteService.buscarPorId(anyLong())).thenReturn(Optional.empty());

    // Act & Assert
    mockMvc.perform(get("/api/clientes/999"))
      .andExpect(status().isNotFound());

    verify(clienteService, times(1)).buscarPorId(999L);
  }

  @Test
  @DisplayName("Deve criar cliente válido")
  void deveCriarClienteValido() throws Exception {
    // Arrange
    Cliente novoCliente = new Cliente(null, "João", "joao@email.com", true, LocalDateTime.now(), null);
    when(clienteService.cadastrar(any(Cliente.class))).thenReturn(cliente);

    // Act & Assert
    mockMvc.perform(post("/api/clientes")
      .contentType(MediaType.APPLICATION_JSON)
      .content(objectMapper.writeValueAsString(novoCliente)))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.nome").value("João"));

    verify(clienteService, times(1)).cadastrar(any(Cliente.class));
  }

  @Test
  @DisplayName("Deve retornar 400 para cliente inválido")
  void deveRetornar400ParaClienteInvalido() throws Exception {
    // Arrange
    Cliente produtoInvalido = new Cliente(null, "", "", true, LocalDateTime.now(), null);
    when(clienteService.cadastrar(any(Cliente.class))).thenThrow(new IllegalArgumentException("Cliente inválido"));

    // Act & Assert
    mockMvc.perform(post("/api/clientes")
      .contentType(MediaType.APPLICATION_JSON)
      .content(objectMapper.writeValueAsString(produtoInvalido)))
      .andExpect(status().isBadRequest());
  }

  @Test
  @DisplayName("Deve desativar cliente ativo")
  void deveDesativarClienteAtivo() throws Exception {
    // Arrange
    doNothing().when(clienteService).ativarDesativar(1L);

    // Act & Assert
    mockMvc.perform(delete("/api/clientes/1"))
      .andExpect(status().isNoContent());

    verify(clienteService, times(1)).ativarDesativar(1L);
  }

  @Test
  @DisplayName("Deve retornar 404 ao desativar cliente inexistente")
  void deveRetornar404AoDesativarClienteInexistente() throws Exception {

    // Act & Assert
    mockMvc.perform(delete("/api/clientes/999"))
      .andExpect(status().isNotFound());

    verify(clienteService, times(1)).ativarDesativar(999L);
  }
}