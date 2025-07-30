package com.deliverytech.delivery_api.security;

import com.deliverytech.delivery_api.model.Role;
import com.deliverytech.delivery_api.model.Usuario;
import com.deliverytech.delivery_api.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.time.LocalDateTime;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UsuarioDetailsServiceImplTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioDetailsServiceImpl usuarioDetailsService;

    private Usuario usuario;

    @BeforeEach
    void setUp() {
        usuario = new Usuario(null, "email@example.com", "senha", "nome", Role.CLIENTE, true, LocalDateTime.now(), null);
    }

    @Test
    void deveCarregarUsuarioComSucesso() {
        // Arrange
        when(usuarioRepository.findByEmail(anyString())).thenReturn(java.util.Optional.of(usuario));

        // Act
        UserDetails userDetails = usuarioDetailsService.loadUserByUsername("email@example.com");

        // Assert
        assertEquals(usuario.getEmail(), userDetails.getUsername());
        assertEquals(usuario.getSenha(), userDetails.getPassword());
        
        assertNotNull(userDetails.getAuthorities());
        assertFalse(userDetails.getAuthorities().isEmpty());
        assertEquals(1, userDetails.getAuthorities().size());

        GrantedAuthority expectedAuthority = new SimpleGrantedAuthority("ROLE_" + usuario.getRole());
        GrantedAuthority actualAuthority = userDetails.getAuthorities().iterator().next();

        assertEquals(expectedAuthority.getAuthority(), actualAuthority.getAuthority());
    }

    @Test
    void deveLancarExcecaoQuandoUsuarioNaoEncontrado() {
        // Arrange
        when(usuarioRepository.findByEmail(anyString())).thenReturn(java.util.Optional.empty());

        // Act e Assert
        assertThrows(UsernameNotFoundException.class, () -> usuarioDetailsService.loadUserByUsername("email@example.com"));
    }
}
