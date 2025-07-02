package com.deliverytech.delivery_api.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

public enum UserRole {
    CUSTOMER,RESTAURANT,DELIVERY_PERSON
}

@Entity
@Data // Anotação Lombok para getters, setters, etc.
public class Cliente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String email;
	private String senha
    private String telefone;
    private String endereco;
	
	@Enumerated(EnumType.STRING)
	private UserRole role;

    @OneToMany(mappedBy = "cliente")
    private List<Pedido> pedidos;
}