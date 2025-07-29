package com.deliverytech.delivery_api.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
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

import com.deliverytech.delivery_api.model.Produto;
import com.deliverytech.delivery_api.model.Restaurante;
import com.deliverytech.delivery_api.security.JwtUtil;
import com.deliverytech.delivery_api.service.impl.ProdutoServiceImpl;
import com.deliverytech.delivery_api.service.impl.RestauranteServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(
  controllers = ProdutoController.class,
  excludeAutoConfiguration = {SecurityAutoConfiguration.class}
)
class ProdutoControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private ProdutoServiceImpl produtoService;

  @MockBean
  private RestauranteServiceImpl restauranteServiceImpl;

  @Autowired
  private ObjectMapper objectMapper;

  @MockBean
  private JwtUtil jwtUtil;

  @MockBean
  private UserDetailsService userDetailsService;

  private Produto produto;
  private Restaurante restaurante;

  @BeforeEach
  void setUp() {
    restaurante = new Restaurante(null, "Restaurante A", "Pizzaria", "123456789", new BigDecimal(10.00), 30, true, null);
    produto = new Produto(null, "Pizza Margherita", "Pizza", "Pizza com molho de tomate e mussarela", new BigDecimal(25.90), true, restaurante);
  }

  @Test
  @DisplayName("Deve buscar todos os produtos")
  void deveBuscarTodosOsProdutos() throws Exception {
    // Arrange
    when(produtoService.buscarTodos()).thenReturn(Arrays.asList(produto));

    // Act & Assert
    mockMvc.perform(get("/api/produtos"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$").isArray())
      .andExpect(jsonPath("$[0].nome").value("Pizza Margherita"))
      .andExpect(jsonPath("$[0].preco").value(25.90));

    verify(produtoService, times(1)).buscarTodos();
  }

  @Test
  @DisplayName("Deve buscar produto por ID")
  void deveBuscarProdutoPorId() throws Exception {
    // Arrange
    when(produtoService.buscarPorId(1L)).thenReturn(Optional.of(produto));

    // Act & Assert
    mockMvc.perform(get("/api/produtos/1"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.nome").value("Pizza Margherita"))
      .andExpect(jsonPath("$.preco").value(25.90));

    verify(produtoService, times(1)).buscarPorId(1L);
  }

  @Test
  @DisplayName("Deve buscar produtos por restaurante")
  void deveBuscarProdutosPorRestaurante() throws Exception {
    // Arrange
    when(produtoService.buscarPorRestaurante(1L)).thenReturn(Arrays.asList(produto));

    // Act & Assert
    mockMvc.perform(get("/api/produtos/restaurante/1"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$").isArray())
      .andExpect(jsonPath("$[0].nome").value("Pizza Margherita"))
      .andExpect(jsonPath("$[0].preco").value(25.90));

    verify(produtoService, times(1)).buscarPorRestaurante(1L);
  }

  @Test
  @DisplayName("Deve retornar 404 para produto não encontrado")
  void deveRetornar404ParaProdutoNaoEncontrado() throws Exception {
    // Arrange
    when(produtoService.buscarPorId(anyLong())).thenReturn(Optional.empty());

    // Act & Assert
    mockMvc.perform(get("/api/produtos/999"))
      .andExpect(status().isNotFound());

    verify(produtoService, times(1)).buscarPorId(999L);
  }

  @Test
  @DisplayName("Deve criar produto válido")
  void deveCriarProdutoValido() throws Exception {
    // Arrange
    Produto novoProduto = new Produto(null, "Hambúrguer", "Lanche", "Hambúrguer com queijo, alface e tomate", new BigDecimal(15.90), true, restaurante);
    when(restauranteServiceImpl.buscarPorId(novoProduto.getRestaurante().getId())).thenReturn(Optional.of(restaurante));
    when(produtoService.cadastrar(any(Produto.class))).thenReturn(novoProduto);

    // Act & Assert
    mockMvc.perform(post("/api/produtos")
      .contentType(MediaType.APPLICATION_JSON)
      .content(objectMapper.writeValueAsString(novoProduto)))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.nome").value("Hambúrguer"));

    verify(produtoService, times(1)).cadastrar(any(Produto.class));
  }

  @Test
  @DisplayName("Deve retornar 400 para produto inválido")
  void deveRetornar400ParaProdutoInvalido() throws Exception {
    // Arrange
    Produto produtoInvalido = new Produto(null, "", "", "", new BigDecimal(15.90), true, null);
    when(produtoService.cadastrar(any(Produto.class))).thenThrow(new IllegalArgumentException("Produto inválido"));

    // Act & Assert
    mockMvc.perform(post("/api/produtos")
      .contentType(MediaType.APPLICATION_JSON)
      .content(objectMapper.writeValueAsString(produtoInvalido)))
      .andExpect(status().isBadRequest());
  }

  @Test
  @DisplayName("Deve atualizar produto existente")
  void deveAtualizarProdutoExistente() throws Exception {
    // Arrange
    Produto produtoAtualizado = new Produto(1L, "Pizza Margherita Atualizada", "Pizza", "Pizza com molho de tomate e mussarela", new BigDecimal(27.90), true, restaurante);
    when(produtoService.atualizar(eq(1L), any(Produto.class))).thenReturn(produtoAtualizado);

    // Act & Assert
    mockMvc.perform(put("/api/produtos/1")
      .contentType(MediaType.APPLICATION_JSON)
      .content(objectMapper.writeValueAsString(produtoAtualizado)))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.nome").value("Pizza Margherita Atualizada"));
  }

  @Test
  @DisplayName("Deve retornar 404 ao atualizar produto não encontrado")
  void deveRetornar404AoAtualizarProdutoNaoEncontrado() throws Exception {
    // Arrange
    Produto produtoAtualizado = new Produto(999L, "Pizza Margherita", "Pizza", "Pizza com molho de tomate e mussarela", new BigDecimal(27.90), true, restaurante);
    when(produtoService.atualizar(1L, any(Produto.class))).thenThrow(new RuntimeException("Produto não encontrado"));

    // Act & Assert
    mockMvc.perform(patch("/api/produtos/999")
      .contentType(MediaType.APPLICATION_JSON)
      .content(objectMapper.writeValueAsString(produtoAtualizado)))
      .andExpect(status().isNotFound());

    verify(produtoService, times(1)).atualizar(999L, produtoAtualizado);
  }

@Test
@DisplayName("Deve alterar disponibilidade de um produto para true")
void deveAlterarDisponibilidadeParaTrue() throws Exception {
    // Arrange
    Long produtoId = 1L;
    boolean novaDisponibilidade = true;

    doNothing().when(produtoService).alterarDisponibilidade(produtoId, novaDisponibilidade);

    // Act & Assert
    mockMvc.perform(patch("/api/produtos/{id}/disponibilidade", produtoId)
      .param("disponivel", String.valueOf(novaDisponibilidade)))
      .andExpect(status().isNoContent());

    verify(produtoService, times(1)).alterarDisponibilidade(produtoId, novaDisponibilidade);
}

  @Test
  @DisplayName("Deve alterar disponibilidade de um produto para false")
  void deveAlterarDisponibilidadeParaFalse() throws Exception {
    // Arrange
    Long produtoId = 1L;
    boolean novaDisponibilidade = false;

    doNothing().when(produtoService).alterarDisponibilidade(produtoId, novaDisponibilidade);

    // Act & Assert
    mockMvc.perform(patch("/api/produtos/{id}/disponibilidade", produtoId)
      .param("disponivel", String.valueOf(novaDisponibilidade)))
      .andExpect(status().isNoContent());

    verify(produtoService, times(1)).alterarDisponibilidade(produtoId, novaDisponibilidade);
  }
}