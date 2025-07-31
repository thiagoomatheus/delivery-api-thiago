package com.deliverytech.delivery_api.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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

import com.deliverytech.delivery_api.model.Role;
import com.deliverytech.delivery_api.model.Usuario;
import com.deliverytech.delivery_api.security.JwtUtil;
import com.deliverytech.delivery_api.service.impl.UsuarioServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(
  controllers = UsuarioController.class,
  excludeAutoConfiguration = {SecurityAutoConfiguration.class}
)
class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    @MockBean
    private UsuarioServiceImpl usuarioService;

    @Autowired
    private ObjectMapper objectMapper;
    
    private Usuario usuario;

    @MockBean
    private JwtUtil jwtUtil;

    @MockBean
    private UserDetailsService userDetailsService;

    @BeforeEach
    void setUp () {
        usuario = new Usuario(1L, "email", "senha", "nome", Role.ADMIN, true, LocalDateTime.now(), null);
    }

    @Test
    @DisplayName("Deve buscar todos os usuários")
    void deveBuscarTodosOsUsuarios() throws Exception {

        when(usuarioService.listarTodos()).thenReturn(Arrays.asList(usuario));

        mockMvc.perform(get("/api/usuarios"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].id").value(1));
    }

    @Test
    @DisplayName("Deve buscar um usuário por id")
    void deveBuscarUmUsuarioPorId() throws Exception {

        when(usuarioService.buscarPorId(anyLong())).thenReturn(Optional.of(usuario));

        mockMvc.perform(get("/api/usuarios/1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id")
        .value(1));
    }

    @Test
    @DisplayName("Deve cadastrar um novo usuário")
    void deveCadastrarUmNovoUsuario() throws Exception {

        when(usuarioService.cadastrar(any(Usuario.class))).thenReturn(usuario);

        mockMvc.perform(post("/api/usuarios")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(usuario)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id")
        .value(1));
    }

    @Test
    @DisplayName("Deve atualizar um usuário")
    void deveAtualizarUmUsuario() throws Exception {

        when(usuarioService.atualizar(anyLong(), any(Usuario.class))).thenReturn(usuario); 

        mockMvc.perform(put("/api/usuarios/1")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(usuario)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id")
        .value(1));
    }

    @Test
    @DisplayName("Deve deletar um usuário")
    void deveDeletarUmUsuario() throws Exception {

        doNothing().when(usuarioService).deletar(anyLong());

        mockMvc.perform(delete("/api/usuarios/1"))
        .andExpect(status().isNoContent());
    }
    
}
