package com.deliverytech.delivery_api.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

import com.deliverytech.delivery_api.model.Usuario;
import com.deliverytech.delivery_api.model.Role;

class UsuarioTest {

    @Test
    void deveCriarUsuario() {

        LocalDateTime dataCriacao = LocalDateTime.now();

        Usuario usuario = new Usuario(1L, "joao@email.com", "123456", "João", Role.ADMIN, true, dataCriacao, null);
        assertNotNull(usuario);
        assertEquals(1L, usuario.getId());
        assertEquals("João", usuario.getNome());
        assertEquals("123456", usuario.getSenha());
        assertEquals(Role.ADMIN, usuario.getRole());
        assertEquals("joao@email.com", usuario.getEmail());
        assertEquals(true, usuario.getAtivo());
        assertEquals(dataCriacao, usuario.getDataCriacao());
    }

    @Test
    void deveTestarSetters() {
        
        LocalDateTime dataCriacao = LocalDateTime.now();

        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNome("João");
        usuario.setSenha("123456");
        usuario.setRole(Role.ADMIN);
        usuario.setEmail("joao@email.com");
        usuario.setAtivo(true);
        usuario.setDataCriacao(dataCriacao);
        assertNotNull(usuario);
        assertEquals(1L, usuario.getId());
        assertEquals("João", usuario.getNome());
        assertEquals("123456", usuario.getSenha());
        assertEquals(Role.ADMIN, usuario.getRole());
        assertEquals("joao@email.com", usuario.getEmail());
        assertEquals(true, usuario.getAtivo());
        assertEquals(dataCriacao, usuario.getDataCriacao());
    }
    
}
