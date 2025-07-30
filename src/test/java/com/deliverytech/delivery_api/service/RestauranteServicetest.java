package com.deliverytech.delivery_api.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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

import com.deliverytech.delivery_api.model.Restaurante;
import com.deliverytech.delivery_api.repository.RestauranteRepository;
import com.deliverytech.delivery_api.service.impl.RestauranteServiceImpl;

@ExtendWith(MockitoExtension.class)
class RestauranteServiceTest {
    
    @Mock
    private RestauranteRepository restauranteRepository;

    @InjectMocks
    private RestauranteServiceImpl restauranteService;

    private Restaurante restaurante;
    
    @BeforeEach
    void setUp() {
        restaurante = new Restaurante(null, "Restaurante A", "Pizzaria", "123456789", new BigDecimal(10.00), 30, true, null);
    }

    @Test
    @DisplayName("Deve listar todos os restaurantes")
    void deveListarTodosOsRestaurantes() {
        // Arrange
        when(restauranteRepository.findAll()).thenReturn(Arrays.asList(restaurante));

        // Act
        List<Restaurante> restaurantes = restauranteService.listarTodos();

        // Assert
        assertNotNull(restaurantes);
        assertEquals(1, restaurantes.size());
        assertEquals("Restaurante A", restaurantes.get(0).getNome());
        verify(restauranteRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Deve buscar restaurante por id")
    void deveBuscarRestaurantePorId() {
        // Arrange
        when(restauranteRepository.findById(1L)).thenReturn(Optional.of(restaurante));

        // Act
        Optional<Restaurante> restauranteEncontrado = restauranteService.buscarPorId(1L);

        // Assert
        assertTrue(restauranteEncontrado.isPresent());
        assertEquals("Restaurante A", restauranteEncontrado.get().getNome());
        verify(restauranteRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Deve listar restaurantes por categoria")
    void deveListarRestaurantesPorCategoria() {
        // Arrange
        when(restauranteRepository.findByCategoria("Pizzaria")).thenReturn(Arrays.asList(restaurante));

        // Act
        List<Restaurante> restaurantes = restauranteService.buscarPorCategoria("Pizzaria");

        // Assert
        assertNotNull(restaurantes);
        assertEquals(1, restaurantes.size());
        assertEquals("Restaurante A", restaurantes.get(0).getNome());
        verify(restauranteRepository, times(1)).findByCategoria("Pizzaria");
    }

    @Test
    @DisplayName("Deve salvar restaurante")
    void deveSalvarRestaurante() {
        // Arrange
        when(restauranteRepository.save(any(Restaurante.class))).thenReturn(restaurante);

        // Act
        Restaurante restauranteSalvo = restauranteService.cadastrar(restaurante);

        // Assert
        assertNotNull(restauranteSalvo);
        assertEquals("Restaurante A", restauranteSalvo.getNome());
        verify(restauranteRepository, times(1)).save(any(Restaurante.class));
    }

    @Test
    @DisplayName("Deve atualizar restaurante")
    void deveAtualizarRestaurante() {
        // Arrange
        when(restauranteRepository.findById(1L)).thenReturn(Optional.of(restaurante));
        when(restauranteRepository.save(any(Restaurante.class))).thenReturn(restaurante);

        // Act
        Restaurante restauranteAtualizado = restauranteService.atualizar(1L, restaurante);

        // Assert
        assertNotNull(restauranteAtualizado);
        assertEquals("Restaurante A", restauranteAtualizado.getNome());
        verify(restauranteRepository, times(1)).findById(1L);
        verify(restauranteRepository, times(1)).save(any(Restaurante.class));
    }
    
}
