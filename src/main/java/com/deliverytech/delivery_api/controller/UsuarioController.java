package com.deliverytech.delivery_api.controller;

import com.deliverytech.delivery_api.dto.request.UsuarioRequest;
import com.deliverytech.delivery_api.dto.response.UsuarioResponse;
import com.deliverytech.delivery_api.model.Usuario;
import com.deliverytech.delivery_api.service.impl.UsuarioServiceImpl;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "Usuários", description = "Operações relacionadas aos usuários")
@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    @Autowired
    private UsuarioServiceImpl usuarioService;

    @PostMapping
    @Operation(summary = "Criar um novo usuário", description = "Cria um novo usuário com base nos dados fornecidos.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Usuário criado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
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
    @Operation(summary = "Listar todos os usuários", description = "Retorna uma lista de todos os usuários cadastrados.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de usuários retornada com sucesso"),
        @ApiResponse(responseCode = "404", description = "Nenhum usuário encontrado")
    })
    public ResponseEntity<List<UsuarioResponse>> listarTodosUsuarios() {
        List<Usuario> usuarios = usuarioService.listarTodos();
        List<UsuarioResponse> responses = usuarios.stream()
            .map(u -> new UsuarioResponse(u.getId(), u.getNome(), u.getEmail(), u.getRole(), u.getRestauranteId()))
            .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    @Operation(
        summary = "Buscar um usuário por ID",
        description = "Retorna os detalhes de um usuário especifico pelo ID."
    )
    @ApiResponses(
        value = {
            @ApiResponse(responseCode = "200", description = "Usuário encontrado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Usuário nao encontrado")
        }
    )
    public ResponseEntity<UsuarioResponse> buscarUsuarioPorId(
        @Parameter(
            name = "id",
            required = true,
            example = "1"
        )
        @PathVariable Long id
    ) {
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
    @Operation(summary = "Atualizar um usuário", description = "Atualiza os detalhes de um usuário existente.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuário atualizado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Usuário nao encontrado")
    })
    public ResponseEntity<UsuarioResponse> atualizarUsuario(
        @Parameter(
            name = "id",
            required = true,
            example = "1"
        )
        @PathVariable Long id,
        @Parameter(
            name = "request",
            required = true,
            example = "{\"nome\": \"João\", \"email\": \"joao@email\", \"senha\": \"123456\", \"role\": \"CLIENTE\", \"restauranteId\": 1}"
        )
        @Valid @RequestBody UsuarioRequest request
    ) {
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
    @Operation(summary = "Deletar um usuário", description = "Deleta um usuário pelo ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Usuário deletado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Usuário nao encontrado")
    })
    public ResponseEntity<Void> deletarUsuario(
        @Parameter(
            name = "id",
            required = true,
            example = "1"
        )
        @PathVariable Long id
    ) {
        usuarioService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}