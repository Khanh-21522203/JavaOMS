package com.javaOMS.order_service.service;

import com.javaOMS.order_service.client.InventoryClient;
import com.javaOMS.order_service.dto.OrderRequest;
import com.javaOMS.order_service.event.OrderPlaceEvent;
import com.javaOMS.order_service.model.Order;
import com.javaOMS.order_service.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;
    private final InventoryClient inventoryClient;
    private final KafkaTemplate<String, OrderPlaceEvent> kafkaTemplate;

    public void placeOrder(OrderRequest orderRequest){

        var isProductInStock = inventoryClient.isInStock(orderRequest.skuCode(), orderRequest.quantity());
        if (isProductInStock) {
            // map OrderRequest to Order Object
            Order order = Order.builder()
                    .orderNumber(UUID.randomUUID().toString())
                    .price(orderRequest.price())
                    .skuCode(orderRequest.skuCode())
                    .quantity(orderRequest.quantity())
                    .build();
            // save order to OrderRepository
            orderRepository.save(order);
            // Send the message to Kafka Topic
            OrderPlaceEvent orderPlaceEvent = OrderPlaceEvent.builder()
                    .orderNumber(order.getOrderNumber())
                    .email(orderRequest.userDetails().email())
                    .build();
            log.info("Start - Sending OrderPlaceEvent {} to Kafka topic order-placed", orderPlaceEvent);
            kafkaTemplate.send("order-placed", orderPlaceEvent);
            log.info("End - Sending OrderPlaceEvent {} to Kafka topic order-placed", orderPlaceEvent);
        } else {
            throw new RuntimeException("Product with SkuCode " + orderRequest.skuCode() + " is not in stock");
        }
    }
}
