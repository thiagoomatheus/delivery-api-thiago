package com.deliverytech.delivery_api.controller;

import com.deliverytech.delivery_api.dto.request.RestauranteRequest;
import com.deliverytech.delivery_api.dto.response.RestauranteResponse;
import com.deliverytech.delivery_api.model.Restaurante;
import com.deliverytech.delivery_api.service.RestauranteService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "Restaurante", description = "Endpoints para gerenciamento de restaurantes")
@RestController
@RequestMapping("/api/restaurantes")
@RequiredArgsConstructor
public class RestauranteController {

    private final RestauranteService restauranteService;

    @PostMapping
    @Operation(
        summary = "Cadastrar um novo restaurante",
        description = "Cria um novo restaurante com as informações fornecidas. O telefone deve ser único."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Restaurante cadastrado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Telefone já cadastrado ou dados inválidos")
    })
    public ResponseEntity<RestauranteResponse> cadastrar(
        @Parameter(
            description = "Informações do novo restaurante",
            example = "{\"nome\": \"Pizzaria do João\", \"telefone\": \"123456789\", \"categoria\": \"Pizzas\", \"taxaEntrega\": 5.00, \"tempoEntregaMinutos\": 30}",
            required = true
        )
        @Valid @RequestBody RestauranteRequest request
    ) {
        Restaurante restaurante = Restaurante.builder()
            .nome(request.getNome())
            .telefone(request.getTelefone())
            .categoria(request.getCategoria())
            .taxaEntrega(request.getTaxaEntrega())
            .tempoEntregaMinutos(request.getTempoEntregaMinutos())
            .ativo(true)
            .build();
        Restaurante salvo = restauranteService.cadastrar(restaurante);
        return ResponseEntity.ok(new RestauranteResponse(
            salvo.getId(), salvo.getNome(), salvo.getCategoria(), salvo.getTelefone(),
            salvo.getTaxaEntrega(), salvo.getTempoEntregaMinutos(), salvo.getAtivo()));
    }

    @GetMapping
    @Operation(summary = "Listar todos os restaurantes", description = "Retorna uma lista de restaurantes cadastrados")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de restaurantes retornada com sucesso"),
        @ApiResponse(responseCode = "404", description = "Nenhum restaurante encontrado")
    })
    public List<RestauranteResponse> listarTodos() {
        return restauranteService.listarTodos().stream()
            .map(r -> new RestauranteResponse(r.getId(), r.getNome(), r.getCategoria(), r.getTelefone(), r.getTaxaEntrega(), r.getTempoEntregaMinutos(), r.getAtivo()))
            .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar restaurante por ID", description = "Retorna os detalhes de um restaurante específico")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Restaurante encontrado"),
        @ApiResponse(responseCode = "404", description = "Restaurante não encontrado")
    })
    public ResponseEntity<RestauranteResponse> buscarPorId(
        @Parameter(description = "ID do restaurante", example = "1", required = true)
        @PathVariable Long id
    ) {
        return restauranteService.buscarPorId(id)
            .map(r -> new RestauranteResponse(r.getId(), r.getNome(), r.getCategoria(), r.getTelefone(), r.getTaxaEntrega(), r.getTempoEntregaMinutos(), r.getAtivo()))
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/categoria/{categoria}")
    @Operation(summary = "Buscar restaurantes por categoria", description = "Retorna uma lista de restaurantes cadastrados com base na categoria fornecida")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de restaurantes por categoria retornada com sucesso"),
        @ApiResponse(responseCode = "404", description = "Nenhum restaurante encontrado para a categoria")
    })
    public List<RestauranteResponse> buscarPorCategoria(
        @Parameter(description = "Categoria do restaurante", example = "Pizzas", required = true)
        @PathVariable String categoria
    ) {
        return restauranteService.buscarPorCategoria(categoria).stream()
            .map(r -> new RestauranteResponse(r.getId(), r.getNome(), r.getCategoria(), r.getTelefone(), r.getTaxaEntrega(), r.getTempoEntregaMinutos(), r.getAtivo()))
            .collect(Collectors.toList());
    }

    @PutMapping("/{id}")
    @Operation(
        summary = "Atualizar um restaurante",
        description = "Atualiza as informações de um restaurante existente."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Restaurante atualizado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Restaurante não encontrado")
    })
    public ResponseEntity<RestauranteResponse> atualizar(
        @Parameter(
            description = "ID do restaurante e informações atualizadas",
            example = "id: 1, {\"nome\": \"Pizzaria do João\", \"telefone\": \"987654321\", \"categoria\": \"Pizzas\", \"taxaEntrega\": 6.00, \"tempoEntregaMinutos\": 25}",
            required = true
        )
        @PathVariable Long id, @Valid @RequestBody RestauranteRequest request
    ) {
        Restaurante atualizado = Restaurante.builder()
            .nome(request.getNome())
            .telefone(request.getTelefone())
            .categoria(request.getCategoria())
            .taxaEntrega(request.getTaxaEntrega())
            .tempoEntregaMinutos(request.getTempoEntregaMinutos())
            .build();
        Restaurante salvo = restauranteService.atualizar(id, atualizado);
        return ResponseEntity.ok(new RestauranteResponse(salvo.getId(), salvo.getNome(), salvo.getCategoria(), salvo.getTelefone(), salvo.getTaxaEntrega(), salvo.getTempoEntregaMinutos(), salvo.getAtivo()));
    }
}