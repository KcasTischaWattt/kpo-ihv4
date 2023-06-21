package com.vkusnoitochka.ordermanagment.controllers;

import com.vkusnoitochka.ordermanagment.exceptions.InvalidDataException;
import com.vkusnoitochka.ordermanagment.handleservice.HandleTokenService;
import com.vkusnoitochka.ordermanagment.models.Order;
import com.vkusnoitochka.ordermanagment.requests.CreateOrderRequest;
import com.vkusnoitochka.ordermanagment.responses.OrderResponse;
import com.vkusnoitochka.ordermanagment.services.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;

@RestController
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;

    private final HandleTokenService handleTokenService;

    @Autowired
    public OrderController(OrderService orderService, HandleTokenService handleTokenService) {
        this.orderService = orderService;
        this.handleTokenService = handleTokenService;
    }

    @PostMapping
    public ResponseEntity<String> createOrder(@RequestBody @NotNull CreateOrderRequest request, @NotNull HttpServletRequest token) {
        try {
            String result = handleTokenService.handleToken(token);
            // Так как библиотеки для обработки JSON у меня подключаться не хотели, пришлось это костылить вот так
            assert result != null;
            Integer id = Integer.parseInt(result.substring(result.indexOf(':') + 1, result.indexOf(',')));
            request.setUserId(id);
            Order createdOrder = orderService.createOrder(request);
            return ResponseEntity.status(HttpStatus.CREATED).body("Order created successfully. Order ID: " + createdOrder.getId());
        } catch (InvalidDataException | URISyntaxException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> getOrder(@PathVariable Integer orderId, @NotNull HttpServletRequest token) {
            Order order = orderService.getOrder(orderId);
            if (order != null) {
                OrderResponse response = new OrderResponse(order.getId(), order.getStatus());
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.notFound().build();
            }
    }


}

