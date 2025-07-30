package com.deliverytech.delivery_api.security;

import com.deliverytech.delivery_api.model.Role;
import com.deliverytech.delivery_api.model.Usuario;
import io.jsonwebtoken.JwtException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetails;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import static org.mockito.Mockito.*;

class JwtUtilTest {

    private JwtUtil jwtUtil = new JwtUtil();

    @Test
    @DisplayName("Deve gerar token")
    void deveGerarToken() {
        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("username");

        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setRole(Role.CLIENTE);
        usuario.setRestauranteId(1L);

        String token = jwtUtil.generateToken(userDetails, usuario);

        assertEquals(1L, ((Integer) jwtUtil.extractClaim(token, claims -> claims.get("userId", Integer.class))).longValue());
        assertEquals(usuario.getRole().name(), jwtUtil.extractClaim(token, claims -> claims.get("role", String.class)));
    }

    @Test
    @DisplayName("Deve testar se token é valido")
    void deveTestarSeTokenValido() {
        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("username");

        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setRole(Role.CLIENTE);
        usuario.setRestauranteId(1L);

        String token = jwtUtil.generateToken(userDetails, usuario);

        assertEquals(true, jwtUtil.isTokenValid(token, userDetails));
    }

    @Test
    @DisplayName("Deve testar se token é invalido")
    void deveTestarSeTokenInvalido() {
        String token = "invalid-token";
        assertThrows(JwtException.class, () -> jwtUtil.extractUsername(token));
    }
}
