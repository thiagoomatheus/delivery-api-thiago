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

import com.deliverytech.delivery_api.dto.request.RestauranteRequest;
import com.deliverytech.delivery_api.model.Produto;
import com.deliverytech.delivery_api.model.Restaurante;
import com.deliverytech.delivery_api.security.JwtUtil;
import com.deliverytech.delivery_api.service.impl.ProdutoServiceImpl;
import com.deliverytech.delivery_api.service.impl.RestauranteServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(
  controllers = RestauranteController.class,
  excludeAutoConfiguration = {SecurityAutoConfiguration.class}
)
class RestauranteControllerTest {

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

    private Restaurante restaurante;

    @BeforeEach
    void setUp() {
        restaurante = new Restaurante(null, "Restaurante A", "Pizzaria", "123456789", new BigDecimal(10.00), 30, true, null);
    }
    
    @Test
    @DisplayName("Deve buscar todos os restaurantes")
    void deveBuscarTodosOsRestaurante() throws Exception {
        when(restauranteServiceImpl.listarTodos()).thenReturn(Arrays.asList(restaurante));

        mockMvc.perform(get("/api/restaurantes"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].nome").value("Restaurante A"));
    }

    @Test
    @DisplayName("Deve buscar restaurante por ID")
    void deveBuscarRestaurantePorId() throws Exception {
        when(restauranteServiceImpl.buscarPorId(anyLong())).thenReturn(Optional.of(restaurante));

        mockMvc.perform(get("/api/restaurantes/{id}", 1L))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.nome").value("Restaurante A"));

        verify(restauranteServiceImpl, times(1)).buscarPorId(1L);
    }

    @Test
    @DisplayName("Deve cadastrar restaurante")
    void deveCadastrarRestaurante() throws Exception {
        when(restauranteServiceImpl.cadastrar(any(Restaurante.class))).thenReturn(restaurante);

        RestauranteRequest request = new RestauranteRequest("Restaurante A", "Pizzaria", "123456789", new BigDecimal(10.00), 30);

        mockMvc.perform(post("/api/restaurantes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.nome").value("Restaurante A"));

        verify(restauranteServiceImpl, times(1)).cadastrar(any(Restaurante.class));
    }

    @Test
    @DisplayName("Deve atualizar restaurante")
    void deveAtualizarRestaurante() throws Exception {
        RestauranteRequest request = new RestauranteRequest("Restaurante B", "Pizzaria", "123456789", new BigDecimal(10.00), 30);

        restaurante.setNome(request.getNome());

        when(restauranteServiceImpl.buscarPorId(anyLong())).thenReturn(Optional.of(restaurante));
        when(restauranteServiceImpl.atualizar(anyLong(), any(Restaurante.class))).thenReturn(restaurante);


        mockMvc.perform(put("/api/restaurantes/{id}", 1L)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.nome").value(request.getNome()));

        verify(restauranteServiceImpl, times(1)).atualizar(anyLong(), any(Restaurante.class));
    }

    @Test
    @DisplayName("Deve buscar restaurante por categoria")
    void deveBuscarRestaurantePorCategoria() throws Exception {
        when(restauranteServiceImpl.buscarPorCategoria("Pizzaria")).thenReturn(Arrays.asList(restaurante));

        mockMvc.perform(get("/api/restaurantes/categoria/{categoria}", "Pizzaria"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].nome").value("Restaurante A"));

        verify(restauranteServiceImpl, times(1)).buscarPorCategoria("Pizzaria");
    }
    
}
