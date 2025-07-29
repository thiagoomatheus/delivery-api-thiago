package com.deliverytech.delivery_api.controller;

import com.deliverytech.delivery_api.dto.request.PedidoRequest;
import com.deliverytech.delivery_api.dto.response.ItemPedidoResponse;
import com.deliverytech.delivery_api.dto.response.PedidoResponse;
import com.deliverytech.delivery_api.model.*;
import com.deliverytech.delivery_api.service.ClienteService;
import com.deliverytech.delivery_api.service.PedidoService;
import com.deliverytech.delivery_api.service.ProdutoService;
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
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "Pedido", description = "Endpoints para gerenciamento de pedidos")
@RestController
@RequestMapping("/api/pedidos")
@RequiredArgsConstructor
public class PedidoController {

        private final PedidoService pedidoService;
        private final ClienteService clienteService;
        private final RestauranteService restauranteService;
        private final ProdutoService produtoService;

        @Operation(
                summary = "Criar um novo pedido",
                description = "Cria um novo pedido com os itens e informações fornecidas. O cliente e restaurante devem existir."
        )
        @ApiResponses(value = {
                @ApiResponse(responseCode = "200", description = "Pedido criado com sucesso"),
                @ApiResponse(responseCode = "400", description = "Dados inválidos ou cliente/restaurante não encontrado")
        })
        @PostMapping
        public ResponseEntity<PedidoResponse> criar(
                @Parameter(
                        description = "Informações do novo pedido",
                        example = "{\"clienteId\": 1, \"restauranteId\": 2, \"enderecoEntrega\": \"Rua A, 123\", \"itens\": [{\"produtoId\": 3, \"quantidade\": 2}]}",
                        required = true
                )
                @Valid @RequestBody PedidoRequest request
        ) {
                Cliente cliente = clienteService.buscarPorId(request.getClienteId())
                        .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
                Restaurante restaurante = restauranteService.buscarPorId(request.getRestauranteId())
                        .orElseThrow(() -> new RuntimeException("Restaurante não encontrado"));

                List<ItemPedido> itens = request.getItens().stream().map(item -> {
                        Produto produto = produtoService.buscarPorId(item.getProdutoId())
                                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));
                        return ItemPedido.builder()
                                .produto(produto)
                                .quantidade(item.getQuantidade())
                                .precoUnitario(produto.getPreco())
                                .build();
                }).collect(Collectors.toList());

                BigDecimal total = itens.stream()
                        .map(i -> i.getPrecoUnitario().multiply(BigDecimal.valueOf(i.getQuantidade())))
                        .reduce(BigDecimal.ZERO, BigDecimal::add);

                Pedido pedido = Pedido.builder()
                        .cliente(cliente)
                        .restaurante(restaurante)
                        .status(StatusPedido.CRIADO)
                        .total(total)
                        .enderecoEntrega(request.getEnderecoEntrega())
                        .itens(itens)
                        .build();

                Pedido salvo = pedidoService.criar(pedido);
                List<ItemPedidoResponse> itensResp = salvo.getItens().stream()
                        .map(i -> new ItemPedidoResponse(i.getProduto().getId(), i.getProduto().getNome(),
                                i.getQuantidade(), i.getPrecoUnitario()))
                        .collect(Collectors.toList());

                return ResponseEntity.ok(new PedidoResponse(
                        salvo.getId(),
                        cliente.getId(),
                        restaurante.getId(),
                        salvo.getEnderecoEntrega(),
                        salvo.getTotal(),
                        salvo.getStatus(),
                        salvo.getDataPedido(),
                        itensResp));
        }
}