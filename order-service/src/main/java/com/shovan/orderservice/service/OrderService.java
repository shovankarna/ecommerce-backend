package com.shovan.orderservice.service;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shovan.orderservice.dto.OrderLineItemsDto;
import com.shovan.orderservice.dto.OrderRequest;
import com.shovan.orderservice.model.OrderLineItems;
import com.shovan.orderservice.repository.OrderRepository;
import com.shovan.orderservice.model.Order;

@Service
@Transactional
public class OrderService {

    @Autowired
    OrderRepository orderRepository;

    public void placeOrder(OrderRequest orderRequest) {
        
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());
        List<OrderLineItems> orderLineItems = orderRequest.getOrderLineItemsDtoList()
        .stream()
        .map(orderLineItemsDto -> mapToDto(orderLineItemsDto)).toList();
     
        order.setOrderLineItemsList(orderLineItems);
        
        orderRepository.save(order);
    }

    private OrderLineItems mapToDto(OrderLineItemsDto orderLineItemsDto) {

        OrderLineItems orderLineItems = new OrderLineItems();
        orderLineItems.setPrice(orderLineItemsDto.getPrice());
        orderLineItems.setQuantity(orderLineItemsDto.getQuantity());
        orderLineItems.setSkuCode(orderLineItemsDto.getSkuCode());

        return orderLineItems;
    }
}
