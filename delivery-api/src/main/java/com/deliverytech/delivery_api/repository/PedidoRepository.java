package com.deliverytech.delivery_api.repository;

import com.deliverytech.delivery_api.entity.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import com.deliverytech.delivery_api.entity.Cliente;
import com.deliverytech.delivery_api.entity.OrderStatus; 

public interface PedidoRepository extends JpaRepository<Pedido, Long> {
	List<Pedido> findByCliente(Cliente cliente);
	List<Pedido> findByStatus(OrderStatus status);
}