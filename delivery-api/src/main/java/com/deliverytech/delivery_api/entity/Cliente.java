package com.deliverytech.delivery_api.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

public enum UserRole {
    CUSTOMER,RESTAURANT,DELIVERY_PERSON
}

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
    private boolean ativo;
	
	@Enumerated(EnumType.STRING)
	private UserRole role;

    @OneToMany(mappedBy = "cliente")
    private List<Pedido> pedidos;
}