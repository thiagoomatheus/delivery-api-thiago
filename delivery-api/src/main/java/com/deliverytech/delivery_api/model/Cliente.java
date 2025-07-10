package com.deliverytech.delivery_api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;
import java.time.LocalDateTime;

@Entity
@Data
public class Cliente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String email;
    private String telefone;
    private String endereco;
	private LocalDateTime data_cadastro;
    private boolean ativo = true;
	
	public void inativar() {
		this.ativo = false;
	}
	
	@Enumerated(EnumType.STRING)
	private UserRole role;

    @OneToMany(mappedBy = "cliente")
	@JsonIgnore
    private List<Pedido> pedidos;
}