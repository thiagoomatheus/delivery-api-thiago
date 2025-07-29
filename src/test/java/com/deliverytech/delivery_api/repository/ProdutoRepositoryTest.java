package com.deliverytech.delivery_api.repository;

import com.deliverytech.delivery_api.model.Produto;
import com.deliverytech.delivery_api.model.Restaurante;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ProdutoRepositoryTest {

  @Autowired
  private TestEntityManager entityManager;

  @Autowired
  private ProdutoRepository produtoRepository;

  private Produto produto1;
  private Produto produto2;

  @BeforeEach
  void setUp() {

    entityManager.clear();

    produto1 = new Produto(null, "Pizza Margherita", "Pizza", "Pizza com molho de tomate e mussarela", new BigDecimal(25.90), true, null);
    produto2 = new Produto(null, "Pizza Calabresa", "Pizza", "Pizza com molho de tomate, mussarela e calabresa", new BigDecimal(30.90), false, null);

    entityManager.persistAndFlush(produto1);
    entityManager.persistAndFlush(produto2);
  }

  @Test
  @DisplayName("Deve encontrar todos os produtos")
  void deveEncontrarTodosOsProdutos() {
    // Act
    List<Produto> resultado = produtoRepository.findAll();

    // Assert
    assertEquals(2, resultado.size());
    assertTrue(resultado.stream().allMatch(p -> p.getNome().toLowerCase().contains("pizza")));
  }

  @Test
  @DisplayName("Deve encontrar produtos por categoria")
  void deveEncontrarProdutosPorCategoria() {
    // Act
    List<Produto> resultado = produtoRepository.findByCategoria("Pizza");

    // Assert
    assertEquals(2, resultado.size());
  }

  @Test
  @DisplayName("Deve encontrar produtos por disponibilidade")
  void deveEncontrarProdutosPorFaixaDePreco() {
    // Act
    List<Produto> resultado = produtoRepository.findByDisponivelTrue();

    // Assert
    assertEquals(1, resultado.size());
  }

  @Test
  @DisplayName("Deve encontrar produtos por Id do restaurante")
  void deveEncontrarProdutosPorIdRestaurante() {

    // Arrange
    Restaurante restaurante = new Restaurante(null, "Restaurante A", "Pizzaria", "123456789", new BigDecimal(10.00), 30, true, null);
    restaurante = entityManager.persistAndFlush(restaurante);


    Produto produtoRestaurante = new Produto(null, "Salada Caesar", "Salada", "Salada com alface, croutons e molho Caesar", new BigDecimal(18.90), true, restaurante);
    entityManager.persistAndFlush(produtoRestaurante);

    // Act
    List<Produto> resultado = produtoRepository.findByRestauranteId(produtoRestaurante.getRestaurante().getId());

    // Assert
    assertEquals(1, resultado.size());
  }

  @Test
  @DisplayName("Deve salvar produto corretamente")
  void deveSalvarProdutoCorretamente() {
    // Arrange
    Produto novoProduto = new Produto(
        null,
        "Hambúrguer",
        "Lanche",
        "Hambúrguer com queijo, alface e tomate",
        new BigDecimal(15.90),
        true,
        null
    );

    // Act
    Produto produtoSalvo = produtoRepository.save(novoProduto);

    // Assert
    assertNotNull(produtoSalvo.getId());
    assertEquals("Hambúrguer", produtoSalvo.getNome());

    Produto produtoEncontrado = entityManager.find(Produto.class, produtoSalvo.getId());
    assertNotNull(produtoEncontrado);
    assertEquals("Hambúrguer", produtoEncontrado.getNome());
  }
}