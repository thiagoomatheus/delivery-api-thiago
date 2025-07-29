package com.deliverytech.delivery_api.repository;

import com.deliverytech.delivery_api.model.Cliente;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ClienteRepositoryTest {

  @Autowired
  private TestEntityManager entityManager;

  @Autowired
  private ClienteRepository clienteRepository;

  private Cliente cliente1;
  private Cliente cliente2;

  @BeforeEach
  void setUp() {
    cliente1 = new Cliente(null, "João", "joao@email.com", true, LocalDateTime.now(), null);
    cliente2 = new Cliente(null, "Maria", "maria@email.com", false, LocalDateTime.now(), null);

    entityManager.persistAndFlush(cliente1);
    entityManager.persistAndFlush(cliente2);
  }

  @Test
  @DisplayName("Deve encontrar clientes que estejam ativos")
  void deveEncontrarClientesAtivos() {
    // Act
    List<Cliente> resultado = clienteRepository.findByAtivoTrue();

    // Assert
    assertEquals(1, resultado.size());
  }

  @Test
  @DisplayName("Deve encontrar clientes por email")
  void deveEncontrarClientesPorEmail() {
    // Act
    Optional<Cliente> resultado = clienteRepository.findByEmail("joao@email.com");

    // Assert
    assertEquals(true, resultado.isPresent());
    assertTrue(resultado.stream().allMatch(p -> p.getEmail() == "joao@email.com"));
  }

@Test
void deveSetarEObterValoresCorretamente() {
  Cliente cliente = new Cliente();
  cliente.setId(10L);
  cliente.setNome("Ana");
  cliente.setEmail("ana@email.com");
  cliente.setAtivo(true);
  cliente.setDataCriacao(LocalDateTime.now());

  assertEquals(10L, cliente.getId());
  assertEquals("Ana", cliente.getNome());
  assertEquals("ana@email.com", cliente.getEmail());
}

  @Test
  @DisplayName("Deve salvar cliente corretamente")
  void deveSalvarClienteCorretamente() {
    // Arrange
    Cliente novoCliente = new Cliente(null, "Thiago", "thiago@email.com", true, LocalDateTime.now(), null);

    // Act
    Cliente clienteSalvo = clienteRepository.save(novoCliente);

    // Assert
    assertNotNull(clienteSalvo.getId());
    assertEquals("Thiago", clienteSalvo.getNome());

    Cliente clienteEncontrado = entityManager.find(Cliente.class, clienteSalvo.getId());
    assertNotNull(clienteEncontrado);
    assertEquals("Thiago", clienteEncontrado.getNome());
  }
  
  @Test
  @DisplayName("Deve verificar se o email existe")
  void deveVerificarSeEmailExiste() {
    // Act
    boolean existe = clienteRepository.existsByEmail("joao@email.com");
  
    // Assert
    assertTrue(existe);
  }
}
