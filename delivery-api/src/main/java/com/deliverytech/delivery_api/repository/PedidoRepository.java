package com.deliverytech.delivery_api.repository;

import com.deliverytech.delivery_api.entity.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {}