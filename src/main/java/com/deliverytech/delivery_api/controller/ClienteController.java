package com.deliverytech.delivery_api.controller;

import com.deliverytech.delivery_api.dto.request.ClienteRequest;
import com.deliverytech.delivery_api.dto.response.ClienteResponse;
import com.deliverytech.delivery_api.model.Cliente;
import com.deliverytech.delivery_api.service.ClienteService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "Cliente", description = "Endpoints para gerenciamento de clientes")
@RestController
@RequestMapping("/api/clientes")
@RequiredArgsConstructor
public class ClienteController {

    private final ClienteService clienteService;

    @PostMapping
    @Operation(
        summary = "Cadastrar um novo cliente",
        description = "Cria um novo cliente com as informações fornecidas. O email deve ser único."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Cliente cadastrado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Email já cadastrado ou dados inválidos")
    })
    public ResponseEntity<ClienteResponse> cadastrar(
        @Parameter(
            description = "Informações do novo cliente",
            example = "{\"nome\": \"João\", \"email\": \"joao@email\"}",
            required = true
        )
        @Valid @RequestBody ClienteRequest request
    ) {
        Cliente cliente = Cliente.builder()
            .nome(request.getNome())
            .email(request.getEmail())
            .ativo(true)
            .build();
        Cliente salvo = clienteService.cadastrar(cliente);
        return ResponseEntity.ok(new ClienteResponse(salvo.getId(), salvo.getNome(), salvo.getEmail(), salvo.getAtivo()));
    }

    @GetMapping
    @Operation(
        summary = "Listar clientes",
        description = "Retorna uma lista de todos os clientes ativos."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de clientes retornada com sucesso"),
        @ApiResponse(responseCode = "404", description = "Nenhum cliente encontrado")
    })
    public List<ClienteResponse> listar() {
        return clienteService.listarAtivos().stream()
            .map(c -> new ClienteResponse(c.getId(), c.getNome(), c.getEmail(), c.getAtivo()))
            .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    @Operation(
        summary = "Buscar cliente por ID",
        description = "Retorna os detalhes de um cliente específico pelo ID."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Cliente encontrado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    })
    public ResponseEntity<ClienteResponse> buscar(
        @Parameter(
            description = "ID do cliente",
            example = "1",
            required = true
        )
        @PathVariable Long id
    ) {
        return clienteService.buscarPorId(id)
            .map(c -> new ClienteResponse(c.getId(), c.getNome(), c.getEmail(), c.getAtivo()))
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    @Operation(
        summary = "Atualizar um cliente",
        description = "Atualiza as informações de um cliente existente."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Cliente atualizado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    })
    public ResponseEntity<ClienteResponse> atualizar(
        @Parameter(
            description = "ID do cliente a ser atualizado e cliente atualizado",
            example = "1, {\"nome\": \"João\", \"email\": \"joao@email\"}",
            required = true
        )
        @PathVariable Long id, @Valid @RequestBody ClienteRequest request
    ) {
        Cliente atualizado = Cliente.builder()
            .nome(request.getNome())
            .email(request.getEmail())
            .build();
        Cliente salvo = clienteService.atualizar(id, atualizado);
        return ResponseEntity.ok(new ClienteResponse(salvo.getId(), salvo.getNome(), salvo.getEmail(), salvo.getAtivo()));
    }

    @DeleteMapping("/{id}")
    @Operation(
        summary = "Ativar/Desativar cliente",
        description = "Ativa ou desativa um cliente pelo ID."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Cliente ativado/desativado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    })
    public ResponseEntity<Void> ativarDesativar(
        @Parameter(
            description = "ID do cliente",
            example = "1",
            required = true
        )
        @PathVariable Long id
    ) {
        clienteService.ativarDesativar(id);
        return ResponseEntity.noContent().build();
    }
}