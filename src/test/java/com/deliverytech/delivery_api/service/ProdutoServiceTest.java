package com.deliverytech.delivery_api.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
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

import com.deliverytech.delivery_api.model.Produto;
import com.deliverytech.delivery_api.model.Restaurante;
import com.deliverytech.delivery_api.repository.ProdutoRepository;
import com.deliverytech.delivery_api.service.impl.ProdutoServiceImpl;

@ExtendWith(MockitoExtension.class)
class ProdutoServiceTest {

  @Mock
  ProdutoRepository produtoRepository;

  @InjectMocks
  ProdutoServiceImpl produtoService;

  private Produto produto;
  private Restaurante restaurante;

  @BeforeEach
  void setUp() {

    restaurante = new Restaurante(1L, "Restaurante A", "Pizzaria", "123456789", new BigDecimal(10.00), 30, true, null);

    produto = new Produto(1L, "Pizza Margherita", "Pizza", "Pizza com molho de tomate e mussarela", new BigDecimal(25.90), true, restaurante);
  }

  @Test
  @DisplayName("Deve buscar todos os produtos")
  void deveBuscarTodosOsProdutos() {
    // Arrange
    when(produtoRepository.findAll()).thenReturn(Arrays.asList(produto));

    // Act
    List<Produto> produtos = produtoService.buscarTodos();

    // Assert
    assertNotNull(produtos);
    assertEquals(1, produtos.size());
    assertEquals("Pizza Margherita", produtos.get(0).getNome());
    assertEquals("Pizza", produtos.get(0).getCategoria());
    verify(produtoRepository, times(1)).findAll();
  }

  @Test
  @DisplayName("Deve salvar produto válido")
  void deveSalvarProdutoValido() {
    // Arrange
    when(produtoRepository.save(any(Produto.class))).thenReturn(produto);

    // Act
    Produto resultado = produtoService.cadastrar(produto);

    // Assert
    assertNotNull(resultado);
    assertEquals("Pizza Margherita", resultado.getNome());
    assertEquals("Pizza", resultado.getCategoria());
    verify(produtoRepository, times(1)).save(produto);
  }

  @Test
  @DisplayName("Deve lançar exceção para produto com nome inválido")
  void deveLancarExcecaoParaProdutoComNomeInvalido() {
    // Arrange
    Produto produtoInvalido = new Produto(null, "", "teste", "teste", new BigDecimal(25.90), true, null);

    // Act & Assert
    IllegalArgumentException exception = assertThrows(
      IllegalArgumentException.class,
      () -> produtoService.cadastrar(produtoInvalido));

    assertEquals("O nome do produto não pode ser vazio", exception.getMessage());
  }

  @Test
  @DisplayName("Deve lançar exceção para produto com preço inválido")
  void deveLancarExcecaoParaProdutoComPrecoInvalido() {
    // Arrange
    Produto produtoInvalido = new Produto(null, "teste", "teste", "teste", new BigDecimal(-25.90), true, null);

    // Act & Assert
    IllegalArgumentException exception = assertThrows(
      IllegalArgumentException.class,
      () -> produtoService.cadastrar(produtoInvalido));

    assertEquals("O preço do produto deve ser maior que zero", exception.getMessage());
  }

  @Test
  @DisplayName("Deve lançar exceção para produto com categoria inválida")
  void deveLancarExcecaoParaProdutoComCategoriaInvalida() {
    // Arrange
    Produto produtoInvalido = new Produto(null, "teste", "", "teste", new BigDecimal(25.90), true, null);

    // Act & Assert
    IllegalArgumentException exception = assertThrows(
      IllegalArgumentException.class,
      () -> produtoService.cadastrar(produtoInvalido));

    assertEquals("A categoria do produto não pode ser vazia", exception.getMessage());
  }

  @Test
  @DisplayName("Deve lançar exceção para produto com descrição inválida")
  void deveLancarExcecaoParaProdutoComDescricaoInvalida() {
    // Arrange
    Produto produtoInvalido = new Produto(null, "teste", "teste", "", new BigDecimal(25.90), true, null);

    // Act & Assert
    IllegalArgumentException exception = assertThrows(
      IllegalArgumentException.class,
      () -> produtoService.cadastrar(produtoInvalido));

    assertEquals("A descrição do produto não pode ser vazia", exception.getMessage());
  }

  @Test
  @DisplayName("Deve lançar exceção para produto sem restaurante")
  void deveLancarExcecaoParaProdutoSemRestaurante() {
    // Arrange
    Produto produtoInvalido = new Produto(null, "teste", "teste", "teste", new BigDecimal(25.90), true, null);

    // Act & Assert
    IllegalArgumentException exception = assertThrows(
      IllegalArgumentException.class,
      () -> produtoService.cadastrar(produtoInvalido));

    assertEquals("O produto deve estar associado a um restaurante", exception.getMessage());
  }

  @Test
  @DisplayName("Deve buscar todos os produtos de um restaurante")
  void deveBuscarTodosOsProdutosDeUmRestaurante() {
    // Arrange
    List<Produto> produtos = Arrays.asList(
      new Produto(1L, "Pizza Margherita", "Pizza", "Pizza com molho de tomate e mussarela", new BigDecimal(25.90), true, restaurante),
      new Produto(1L, "Pizza Margherita", "Pizza", "Pizza com molho de tomate e mussarela", new BigDecimal(25.90), true, restaurante)
    );
    when(produtoRepository.findByRestauranteId(1L)).thenReturn(produtos);

    // Act
    List<Produto> resultado = produtoService.buscarPorRestaurante(1L);

    // Assert
    assertEquals(2, resultado.size());
  }

  @Test
  @DisplayName("Deve atualizar um produto existente")
  void deveAtualizarProduto() {

    // Arrange
    Produto produtoAtualizado = new Produto(1L, "Pizza Margherita Atualizada", "Pizza", "Pizza com molho de tomate e mussarela e manjericão", new BigDecimal(27.90), true, null);
    when(produtoRepository.findById(1L)).thenReturn(Optional.of(produto));
    when(produtoRepository.save(any(Produto.class))).thenReturn(produtoAtualizado);
    // Act
    Produto resultado = produtoService.atualizar(1L, produtoAtualizado);

    // Assert
    assertEquals("Pizza Margherita Atualizada", resultado.getNome());
  }

  @Test
  @DisplayName("Deve buscar produto por ID")
  void deveBuscarProdutoPorId() {
    // Arrange
    when(produtoRepository.findById(1L)).thenReturn(Optional.of(produto));

    // Act
    Optional<Produto> resultado = produtoService.buscarPorId(1L);

    // Assert
    assertTrue(resultado.isPresent());
    assertEquals("Pizza Margherita", resultado.get().getNome());
    verify(produtoRepository, times(1)).findById(1L);
  }

  @Test
  @DisplayName("Deve alterar disponibilidade de um produto")
  void deveAlterarDisponibilidade() {
    // Arrange
    when(produtoRepository.findById(1L)).thenReturn(Optional.of(produto));

    // Act
    produtoService.alterarDisponibilidade(1L, false);

    // Assert
    assertEquals(false, produto.getDisponivel()); // Verifica o estado do objeto mocapado
    verify(produtoRepository, times(1)).save(produto);
  }
}