package com.deliverytech.delivery_api.controller;

import com.deliverytech.delivery_api.dto.request.UsuarioRequest;
import com.deliverytech.delivery_api.dto.response.UsuarioResponse;
import com.deliverytech.delivery_api.model.Usuario;
import com.deliverytech.delivery_api.service.impl.UsuarioServiceImpl;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioServiceImpl usuarioService;

    @PostMapping
    public ResponseEntity<UsuarioResponse> criarUsuario(
        @Parameter(
            name = "request",
            required = true,
            example = "{\"nome\": \"João\", \"email\": \"joao@email\", \"senha\": \"123456\", \"role\": \"CLIENTE\", \"restauranteId\": 1}"
        )
        @Valid @RequestBody UsuarioRequest request
    ) {
        Usuario novoUsuario = new Usuario();
        novoUsuario.setNome(request.getNome());
        novoUsuario.setEmail(request.getEmail());
        novoUsuario.setSenha(request.getSenha());
        novoUsuario.setRole(request.getRole());
        novoUsuario.setRestauranteId(request.getRestauranteId());
        
        Usuario salvo = usuarioService.cadastrar(novoUsuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(new UsuarioResponse(salvo.getId(), salvo.getNome(), salvo.getEmail(), salvo.getRole(), salvo.getRestauranteId()));
    }

    @GetMapping
    public ResponseEntity<List<UsuarioResponse>> listarTodosUsuarios() {
        List<Usuario> usuarios = usuarioService.listarTodos();
        List<UsuarioResponse> responses = usuarios.stream()
            .map(u -> new UsuarioResponse(u.getId(), u.getNome(), u.getEmail(), u.getRole(), u.getRestauranteId()))
            .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponse> buscarUsuarioPorId(@PathVariable Long id) {
        try {
            Usuario usuario = usuarioService.buscarPorId(id).orElse(null);
            if (usuario == null) {
                return ResponseEntity.notFound().build();
            }
            UsuarioResponse response = new UsuarioResponse(usuario.getId(), usuario.getNome(), usuario.getEmail(), usuario.getRole(), usuario.getRestauranteId());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponse> atualizarUsuario(@PathVariable Long id, @Valid @RequestBody UsuarioRequest request) {
        try {
            Usuario usuarioAtualizado = new Usuario();
            usuarioAtualizado.setNome(request.getNome());
            usuarioAtualizado.setEmail(request.getEmail());
            usuarioAtualizado.setSenha(request.getSenha());
            usuarioAtualizado.setRole(request.getRole());
            
            Usuario usuario = usuarioService.atualizar(id, usuarioAtualizado);
            UsuarioResponse response = new UsuarioResponse(usuario.getId(), usuario.getNome(), usuario.getEmail(), usuario.getRole(), usuario.getRestauranteId());
            return ResponseEntity.ok(response);
        } catch (ResponseStatusException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarUsuario(@PathVariable Long id) {
        usuarioService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}