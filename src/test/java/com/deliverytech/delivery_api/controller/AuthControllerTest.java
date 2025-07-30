package com.deliverytech.delivery_api.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.deliverytech.delivery_api.dto.request.LoginRequest;
import com.deliverytech.delivery_api.dto.request.RegisterRequest;
import com.deliverytech.delivery_api.model.Role;
import com.deliverytech.delivery_api.model.Usuario;
import com.deliverytech.delivery_api.repository.UsuarioRepository;
import com.deliverytech.delivery_api.security.JwtAuthenticationFilter;
import com.deliverytech.delivery_api.security.JwtUtil;
import com.deliverytech.delivery_api.security.UsuarioDetailsServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;


@WebMvcTest(
  controllers = AuthController.class,
  excludeAutoConfiguration = {SecurityAutoConfiguration.class}
)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JwtUtil jwtUtil;

    @MockBean
    private UserDetailsService userDetailsService;

    @MockBean
    private UsuarioDetailsServiceImpl usuarioService;

    @MockBean
    private UsuarioRepository usuarioRepository;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    private Usuario usuario;

    @BeforeEach
    void setUp() {
        usuario = new Usuario(null, "email@email.com", "senha", "nome", Role.CLIENTE, true, LocalDateTime.now(), null);
    }

    @Test
    @DisplayName("Deve criar um novo usuário")
    void deveCriarNovoUsuario() throws Exception {
        // Arrange
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        // Act and Assert
        mockMvc.perform(post("/api/auth/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(usuario)))
            .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Deve retornar erro ao tentar registrar usuário com email já cadastrado")
    void deveRetornarErroEmailJaCadastrado() throws Exception {
        // Arrange
        when(usuarioRepository.findByEmail(usuario.getEmail())).thenReturn(Optional.of(usuario));

        // Act and Assert
        mockMvc.perform(post("/api/auth/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(usuario)))
            .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Deve retonar erro ao tentar autenticar um usuário sem email cadastrado")
    void deveRetornarErroEmailNaoCadastrado() throws Exception {

        when(usuarioRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());

        LoginRequest loginRequest = new LoginRequest("test@example.com", "senha123");

        // Act and Assert
        mockMvc.perform(post("/api/auth/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(loginRequest)))
            .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Deve registrar um novo usuário com sucesso")
    void deveRegistrarNovoUsuarioComSucesso() throws Exception {
        // Arrange
        when(usuarioRepository.findByEmail(usuario.getEmail())).thenReturn(Optional.empty());

        // Act
        MvcResult result = mockMvc.perform(post("/api/auth/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(usuario)))
            .andExpect(status().isOk())
            .andReturn();

        // Assert
        String token = result.getResponse().getContentAsString();
        assertNotNull(token);
    }

    @Test
    @DisplayName("Deve retornar erro ao tentar registrar usuário com email já cadastrado")
    void deveRetornarErroAoTentarRegistrarUsuarioComEmailJaCadastrado() throws Exception {
        // Arrange
        when(usuarioRepository.findByEmail(usuario.getEmail())).thenReturn(Optional.of(new Usuario()));

        // Act
        MvcResult result = mockMvc.perform(post("/api/auth/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(usuario)))
            .andExpect(status().isBadRequest())
            .andReturn();

        // Assert
        String errorMessage = result.getResponse().getContentAsString();
        assertEquals("Email já cadastrado", errorMessage);
    }

    @Test
    @DisplayName("Deve retornar erro ao tentar registrar usuário com dados inválidos")
    void deveRetornarErroAoTentarRegistrarUsuarioComDadosInvalidos() throws Exception {
        // Arrange
        RegisterRequest request = new RegisterRequest("", "", "", null, null);

        // Act
        MvcResult result = mockMvc.perform(post("/api/auth/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest())
            .andReturn();

        // Assert
        String errorMessage = result.getResponse().getContentAsString();
        assertNotNull(errorMessage);
    }
    
}
