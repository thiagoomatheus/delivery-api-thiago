package com.deliverytech.delivery_api.repository;

import com.deliverytech.delivery_api.entity.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {}