package com.deliverytech.delivery_api.controller;

import com.deliverytech.delivery_api.dto.request.ProdutoRequest;
import com.deliverytech.delivery_api.dto.response.ProdutoResponse;
import com.deliverytech.delivery_api.model.Produto;
import com.deliverytech.delivery_api.model.Restaurante;
import com.deliverytech.delivery_api.service.ProdutoService;
import com.deliverytech.delivery_api.service.RestauranteService;

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

@Tag(name = "Produto", description = "Endpoints para gerenciamento de produtos")
@RestController
@RequestMapping("/api/produtos")
@RequiredArgsConstructor
public class ProdutoController {

    private final ProdutoService produtoService;
    private final RestauranteService restauranteService;

    @GetMapping
    @Operation(summary = "Listar todos os produtos", description = "Retorna uma lista de todos os produtos disponíveis.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de produtos retornada com sucesso")
    })
    public List<ProdutoResponse> listarTodos() {
        return produtoService.buscarTodos().stream()
        .map(p -> new ProdutoResponse(p.getId(), p.getNome(), p.getCategoria(), p.getDescricao(), p.getPreco(), p.getDisponivel()))
        .collect(Collectors.toList());
    }

    @PostMapping
    @Operation(
        summary = "Cadastrar um novo produto",
        description = "Cria um novo produto com as informações fornecidas. O restaurante deve existir."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Produto cadastrado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos ou restaurante não encontrado")
    })
    public ResponseEntity<ProdutoResponse> cadastrar(
        @Parameter(
            description = "Informações do novo produto",
            example = "{\"restauranteId\": 1, \"nome\": \"Coca-Cola\", \"categoria\": \"Bebidas\", \"descricao\": \"Coca-Cola 2L\", \"preco\": 5.99}",
            required = true
        )
        @Valid @RequestBody ProdutoRequest request
    ) {
        Restaurante restaurante = restauranteService.buscarPorId(request.getRestauranteId())
            .orElseThrow(() -> new RuntimeException("Restaurante não encontrado"));

        Produto produto = Produto.builder()
            .nome(request.getNome())
            .categoria(request.getCategoria())
            .descricao(request.getDescricao())
            .preco(request.getPreco())
            .disponivel(true)
            .restaurante(restaurante)
            .build();

        Produto salvo = produtoService.cadastrar(produto);
        return ResponseEntity.ok(new ProdutoResponse(
            salvo.getId(), salvo.getNome(), salvo.getCategoria(), salvo.getDescricao(), salvo.getPreco(), salvo.getDisponivel()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar produto por ID", description = "Retorna os detalhes de um produto específico pelo ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Produto encontrado"),
        @ApiResponse(responseCode = "404", description = "Produto não encontrado")
    })
    public ResponseEntity<ProdutoResponse> buscarPorId(
        @Parameter(description = "ID do produto", example = "1", required = true)
        @PathVariable Long id
    ) {
        return produtoService.buscarPorId(id)
            .map(c -> new ProdutoResponse(c.getId(), c.getNome(), c.getCategoria(), c.getDescricao(), c.getPreco(), c.getDisponivel()))
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/restaurante/{restauranteId}")
    @Operation(summary = "Listar produtos por restaurante", description = "Retorna uma lista de produtos de um restaurante")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de produtos retornada com sucesso"),
        @ApiResponse(responseCode = "404", description = "Restaurante não encontrado")
    })
    public List<ProdutoResponse> listarPorRestaurante(
        @Parameter(description = "ID do restaurante", example = "1", required = true)
        @PathVariable Long restauranteId
    ) {
        return produtoService.buscarPorRestaurante(restauranteId).stream()
        .map(p -> new ProdutoResponse(p.getId(), p.getNome(), p.getCategoria(), p.getDescricao(), p.getPreco(), p.getDisponivel()))
        .collect(Collectors.toList());
    }

    @PutMapping("/{id}")
    @Operation(
        summary = "Atualizar um produto",
        description = "Atualiza as informações de um produto existente."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Produto atualizado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Produto não encontrado")
    })
    public ResponseEntity<ProdutoResponse> atualizar(
        @Parameter(
            description = "ID do produto a ser atualizado e produto atualizado",
            example = "id: 1, {\"nome\": \"Coca-Cola\", \"categoria\": \"Bebidas\", \"descricao\": \"Coca-Cola 2L\", \"preco\": 5.99}",
            required = true
        )
        @PathVariable Long id, @Valid @RequestBody ProdutoRequest request
    ) {
        Produto atualizado = Produto.builder()
            .nome(request.getNome())
            .categoria(request.getCategoria())
            .descricao(request.getDescricao())
            .preco(request.getPreco())
            .build();
        Produto salvo = produtoService.atualizar(id, atualizado);
        return ResponseEntity.ok(new ProdutoResponse(salvo.getId(), salvo.getNome(), salvo.getCategoria(), salvo.getDescricao(), salvo.getPreco(), salvo.getDisponivel()));
    }

    @PatchMapping("/{id}/disponibilidade")
    @Operation(
        summary = "Alterar disponibilidade do produto",
        description = "Altera a disponibilidade de um produto."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Disponibilidade alterada com sucesso"),
        @ApiResponse(responseCode = "404", description = "Produto não encontrado")
    })
    public ResponseEntity<Void> alterarDisponibilidade(
        @Parameter(
            description = "ID do produto e disponibilidade",
            example = "id: 1, {\"disponivel\": true}",
            required = true
        )
        @PathVariable Long id, @RequestParam boolean disponivel
    ) {
        produtoService.alterarDisponibilidade(id, disponivel);
        return ResponseEntity.noContent().build();
    }
}
