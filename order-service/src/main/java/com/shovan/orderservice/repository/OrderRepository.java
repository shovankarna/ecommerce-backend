package com.shovan.orderservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shovan.orderservice.model.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
    
}
