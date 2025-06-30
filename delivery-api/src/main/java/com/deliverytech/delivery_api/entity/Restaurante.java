package com.deliverytech.delivery_api.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Entity
@Data // Anotação Lombok para getters, setters, etc.
public class Restaurante {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
    private String nome;
    private String cnpj;
    private String endereco;

    @OneToMany(mappedBy = "restaurante", cascade = CascadeType.ALL)
    private List<Pedido> cardapio;
}