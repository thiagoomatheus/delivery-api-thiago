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

import com.deliverytech.delivery_api.model.Role;
import com.deliverytech.delivery_api.model.Usuario;
import com.deliverytech.delivery_api.repository.UsuarioRepository;
import com.deliverytech.delivery_api.service.impl.UsuarioServiceImpl;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioServiceImpl usuarioServiceImpl;

    private Usuario usuario;

    @BeforeEach
    void setUp() {
        usuario = new Usuario(1L, "email", "senha", "nome", Role.ADMIN, true, LocalDateTime.now(), null);
    }

    @Test
    @DisplayName("Deve salvar um novo usuário")
    void salvarUsuario() {
        when(usuarioRepository.save(any())).thenReturn(usuario);

        assertDoesNotThrow(() -> usuarioServiceImpl.cadastrar(new Usuario()));
        verify(usuarioRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("Deve obter um usuário por Id")
    void obterUsuarioPorEmail() {
        assertNotNull(usuarioServiceImpl.buscarPorId(1L));

        verify(usuarioRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Deve obter todos os usuários")
    void obterTodosUsuarios() {
        when(usuarioRepository.findAll()).thenReturn(Arrays.asList(usuario));
        List<Usuario> usuarios = usuarioServiceImpl.listarTodos();
        assertEquals(1, usuarios.size());
        assertTrue(usuarios.contains(usuario));
        verify(usuarioRepository, times(1)).findAll();
    }
 
    @Test
    @DisplayName("Deve atualizar um usuário")
    void atualizarUsuario() {
        when(usuarioRepository.save(any())).thenReturn(usuario);

        assertDoesNotThrow(() -> usuarioServiceImpl.atualizar(1L, usuario));

        verify(usuarioRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("Deve deletar um usuário")
    void deletarUsuario() {
        assertDoesNotThrow(() -> usuarioServiceImpl.deletar(1L));
        verify(usuarioRepository, times(1)).findById(1L);
    }

}
