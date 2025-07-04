package com.deliverytech.delivery_api.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.math.BigDecimal;

@Entity
@Data // Anotação Lombok para getters, setters, etc.
public class Restaurante {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String categoria;
    private String endereco;
    private String telefone;
    private BigDecimal taxaEntrega;
    private boolean ativo;
	private BigDecimal avaliacao;
	
	public boolean isAtivo() {
		return ativo;
	}
	public Boolean getAtivo() {
		return ativo;
	}

    @OneToMany(mappedBy = "restaurante", cascade = CascadeType.ALL)
	@JsonIgnore
    private List<Produto> produtos;

    @OneToMany(mappedBy = "restaurante")
	@JsonIgnore
    private List<Pedido> pedidos;
}