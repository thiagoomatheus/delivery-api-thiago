package com.deliverytech.delivery_api.entity;

import com.deliverytech.delivery_api.enums.StatusPedido;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.UUID;

@Entity
@Data
public class Pedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	private String numeroPedido;
    private LocalDateTime dataPedido;
    private String enderecoEntrega;
    private BigDecimal subtotal;
    private BigDecimal taxaEntrega;
    private BigDecimal valorTotal;
	private String observacoes;

    @Enumerated(EnumType.STRING)
    private StatusPedido status;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
	@JsonIgnore
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "restaurante_id")
	@JsonIgnore
    private Restaurante restaurante;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL)
	@JsonIgnore
    private List<ItemPedido> itens;
	
	@PrePersist
    public void gerarNumeroPedido() {
        if (numeroPedido == null) {
            this.numeroPedido = "PED-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
            this.dataPedido = LocalDateTime.now();
        }
    }

    public void adicionarItem(ItemPedido item) {
        item.setPedido(this);
        this.itens.add(item);
        calcularTotal();
    }

    public void calcularTotal() {
        this.valorTotal = itens.stream()
            .map(ItemPedido::getSubtotal)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void confirmar() {
        this.status = StatusPedido.CONFIRMADO;
        calcularTotal();
    }
}