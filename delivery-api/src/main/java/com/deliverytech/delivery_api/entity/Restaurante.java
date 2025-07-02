package com.deliverytech.delivery_api.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;

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
	@JsonIgnore
    private List<Pedido> cardapio;
}