package com.deliverytech.delivery_api.service;

import java.util.List;
import java.util.Optional;

import com.deliverytech.delivery_api.model.Usuario;

public interface UsuarioService {
    List<Usuario> listarTodos();
    Optional<Usuario> buscarPorId(Long id);
    Usuario cadastrar(Usuario usuario);
    Usuario atualizar(Long id, Usuario usuario);
    void deletar(Long id);
}
