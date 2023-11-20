package com.shovan.orderservice.service;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import com.shovan.orderservice.config.WebClientConfig;
import com.shovan.orderservice.dto.OrderLineItemsDto;
import com.shovan.orderservice.dto.OrderRequest;
import com.shovan.orderservice.model.OrderLineItems;
import com.shovan.orderservice.repository.OrderRepository;
import com.shovan.orderservice.model.Order;

@Service
public class OrderService {

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    WebClient webClient;

    public void placeOrder(OrderRequest orderRequest) {

        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());
        List<OrderLineItems> orderLineItems = orderRequest.getOrderLineItemsDtoList()
                .stream()
                .map(orderLineItemsDto -> mapToDto(orderLineItemsDto)).toList();

        order.setOrderLineItemsList(orderLineItems);

        //// CALL INVENTORY SERVICE PLACE ORDER IF PRODUCT IS IN STOCK
        Boolean result = webClient
                .get()
                .uri("http://localhost:8082/api/inventory")
                .retrieve()
                .bodyToMono(Boolean.class) // Replace InventoryResponse with the actual class representing the response
                .block();

        if (result) {
            orderRepository.save(order);
        } else {
            throw new IllegalArgumentException("Product is not in stock, please try again later!");
        }
    }

    private OrderLineItems mapToDto(OrderLineItemsDto orderLineItemsDto) {

        OrderLineItems orderLineItems = new OrderLineItems();
        orderLineItems.setPrice(orderLineItemsDto.getPrice());
        orderLineItems.setQuantity(orderLineItemsDto.getQuantity());
        orderLineItems.setSkuCode(orderLineItemsDto.getSkuCode());

        return orderLineItems;
    }
}
