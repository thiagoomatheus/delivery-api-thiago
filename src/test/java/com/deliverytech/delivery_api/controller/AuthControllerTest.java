package com.deliverytech.delivery_api.controller;

import com.deliverytech.delivery_api.dto.request.LoginRequest;
import com.deliverytech.delivery_api.dto.request.RegisterRequest;
import com.deliverytech.delivery_api.model.Role;
import com.deliverytech.delivery_api.model.Usuario;
import com.deliverytech.delivery_api.repository.UsuarioRepository;
import com.deliverytech.delivery_api.security.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AuthController authController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    @DisplayName("Deve registrar um novo usuário com sucesso")
    void deveRegistrarNovoUsuario() throws Exception {
        RegisterRequest request = new RegisterRequest("novo@email.com", "senha123", "Novo Usuário", Role.CLIENTE, null);
        Usuario usuario = Usuario.builder()
                .email(request.getEmail())
                .senha("encodedPassword")
                .nome(request.getNome())
                .role(Role.CLIENTE)
                .ativo(true)
                .build();

        when(usuarioRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(request.getSenha())).thenReturn("encodedPassword");
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);
        when(jwtUtil.generateToken(any(User.class), any(Usuario.class))).thenReturn("mockedJwtToken");

        mockMvc.perform(post("/api/auth/register")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(content().string("mockedJwtToken"));

        verify(usuarioRepository, times(1)).findByEmail(request.getEmail());
        verify(passwordEncoder, times(1)).encode(request.getSenha());
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
        verify(jwtUtil, times(1)).generateToken(any(User.class), any(Usuario.class));
    }

    @Test
    @DisplayName("Não deve registrar usuário se o email já estiver cadastrado")
    void deveNegarRegistroUsuarioComEmailExistente() throws Exception {
        RegisterRequest request = new RegisterRequest("existente@email.com", "senha123", "Usuário Existente", Role.CLIENTE, null);
        Usuario existingUser = Usuario.builder().email(request.getEmail()).build();

        when(usuarioRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(existingUser));

        mockMvc.perform(post("/api/auth/register")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest())
            .andExpect(content().string("Email já cadastrado"));

        verify(usuarioRepository, times(1)).findByEmail(request.getEmail());
        verifyNoInteractions(passwordEncoder);
        verify(usuarioRepository, never()).save(any(Usuario.class));
        verifyNoInteractions(jwtUtil);
    }

    @Test
    @DisplayName("Deve registrar usuário com role padrão CLIENTE se não for especificado")
    void deveRegistrarUsuarioComRolePadrao() throws Exception {
        RegisterRequest request = new RegisterRequest("default@email.com", "senha123", "Default User", null, null);
        Usuario usuario = Usuario.builder()
            .email(request.getEmail())
            .senha("encodedPassword")
            .nome(request.getNome())
            .role(Role.CLIENTE)
            .ativo(true)
            .build();

        when(usuarioRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(request.getSenha())).thenReturn("encodedPassword");
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);
        when(jwtUtil.generateToken(any(User.class), any(Usuario.class))).thenReturn("mockedJwtToken");

        mockMvc.perform(post("/api/auth/register")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(content().string("mockedJwtToken"));

        verify(usuarioRepository, times(1)).save(argThat(u -> u.getRole().equals(Role.CLIENTE)));
    }

    @Test
    @DisplayName("Deve efetuar login com sucesso e retornar JWT")
    void deveEfetuarLogin() throws Exception {
        LoginRequest request = new LoginRequest("teste@email.com", "senha123");
        Usuario usuario = Usuario.builder()
            .email(request.getEmail())
            .senha("encodedPassword")
            .role(Role.CLIENTE)
            .ativo(true)
            .build();

        when(usuarioRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(usuario));
        Authentication authentication = mock(Authentication.class);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(jwtUtil.generateToken(any(User.class), any(Usuario.class))).thenReturn("anotherMockedJwtToken");
        when(passwordEncoder.matches(request.getSenha(), usuario.getSenha())).thenReturn(true);

        mockMvc.perform(post("/api/auth/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(content().string("anotherMockedJwtToken"));

        verify(usuarioRepository, times(1)).findByEmail(request.getEmail());
        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtUtil, times(1)).generateToken(any(User.class), any(Usuario.class));
    }

    @Test
    @DisplayName("Deve retornar 401 para credenciais de login inválidas")
    void deveRetornar401ParaCredenciaisInvalidas() throws Exception {
        LoginRequest request = new LoginRequest("teste@email.com", "senhaInvalida");
        Usuario usuario = Usuario.builder()
            .email(request.getEmail())
            .senha("encodedPassword")
            .role(Role.CLIENTE)
            .ativo(true)
            .build();

        when(usuarioRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(usuario));

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isUnauthorized());

        verify(usuarioRepository, times(1)).findByEmail(request.getEmail());
        verifyNoInteractions(jwtUtil);
    }

    

    @Test
    @DisplayName("Deve retornar 404 para usuário não encontrado no login")
    void deveRetornar404ParaUsuarioNaoEncontrado() throws Exception {
        LoginRequest request = new LoginRequest("naoexiste@email.com", "senha123");

        when(usuarioRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());
        mockMvc.perform(post("/api/auth/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isNotFound());

        verify(usuarioRepository, times(1)).findByEmail(request.getEmail());
        verifyNoInteractions(authenticationManager);
        verifyNoInteractions(jwtUtil);
    }
}