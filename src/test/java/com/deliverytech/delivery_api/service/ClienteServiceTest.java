package com.deliverytech.delivery_api.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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

import com.deliverytech.delivery_api.model.Cliente;
import com.deliverytech.delivery_api.repository.ClienteRepository;
import com.deliverytech.delivery_api.service.impl.ClienteServiceImpl;

@ExtendWith(MockitoExtension.class)
class ClienteServiceTest {

  @Mock
  private ClienteRepository clienteRepository;

  @InjectMocks
  private ClienteServiceImpl clienteService;

  private Cliente cliente;

  @BeforeEach
  void setUp() {
    cliente = new Cliente(1L, "João", "joao@email.com", true, LocalDateTime.now(), null);
  }

  @Test
  @DisplayName("Deve cadastrar cliente válido")
  void deveCadastrarClienteValido() {
    // Arrange
    when(clienteRepository.save(any(Cliente.class))).thenReturn(cliente);

    // Act
    Cliente resultado = clienteService.cadastrar(cliente);

    // Assert
    assertNotNull(resultado);
    assertEquals("João", resultado.getNome());
    assertEquals("joao@email.com", resultado.getEmail());
    verify(clienteRepository, times(1)).save(cliente);
  }

  @Test
  @DisplayName("Deve buscar cliente por ID")
  void deveBuscarClientePorId() {
    // Arrange
    when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));

    // Act
    Optional<Cliente> resultado = clienteService.buscarPorId(1L);

    // Assert
    assertTrue(resultado.isPresent());
    assertEquals("João", resultado.get().getNome());
    verify(clienteRepository, times(1)).findById(1L);
  }

  @Test
  @DisplayName("Deve buscar todos os clientes ativos")
  void deveBuscarTodosOsClientesAtivos() {
    // Arrange
    List<Cliente> clientes = Arrays.asList(
      new Cliente(1L, "João", "joao@email.com", true, LocalDateTime.now(), null));
      new Cliente(2L, "Maria", "maria@email.com", false, LocalDateTime.now(), null);
    when(clienteRepository.findByAtivoTrue()).thenReturn(clientes);

    // Act
    List<Cliente> resultado = clienteService.listarAtivos();

    // Assert
    assertEquals(1, resultado.size());
    verify(clienteRepository, times(1)).findByAtivoTrue();
  }
  
  @Test
  @DisplayName("Deve atualizar cliente existente")
  void deveAtualizarClienteExistente() {
    // Arrange
    Cliente clienteAtualizado = new Cliente(1L, "João Atualizado", "joao@email.com", true, null, null);
    when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
    
    when(clienteRepository.save(any(Cliente.class))).thenAnswer(invocation -> invocation.getArgument(0));

    // Act
    
    Cliente resultado = clienteService.atualizar(1L, clienteAtualizado);

    // Assert
    assertEquals("João Atualizado", resultado.getNome());
    assertEquals("joao@email.com", resultado.getEmail());
    verify(clienteRepository, times(1)).findById(1L);
    verify(clienteRepository, times(1)).save(any(Cliente.class));
  }

  @Test
  @DisplayName("Deve desativar cliente existente")
  void deveDesativarClienteExistente() {
    // Arrange
    when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));

    // Act
    assertDoesNotThrow(() -> clienteService.ativarDesativar(1L));

    // Assert
    verify(clienteRepository, times(1)).findById(1L);
    verify(clienteRepository, times(1)).save(any(Cliente.class));
  }
}