package ru.yandex.practicum.order.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.api.order.CreateOrderRequest;
import ru.yandex.practicum.api.order.OrderDto;
import ru.yandex.practicum.api.order.ProductReturnRequest;
import ru.yandex.practicum.order.service.OrderService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping
    public List<OrderDto> getOrdersByUser(@RequestParam("username") String username) {
        return orderService.getOrdersByUsername(username);
    }

    @PutMapping
    public OrderDto createOrder(@RequestBody CreateOrderRequest request) {
        return orderService.createOrder(request);
    }

    @PostMapping("/return")
    public OrderDto returnOrder(@RequestBody ProductReturnRequest request) {
        return orderService.returnOrder(request);
    }

    @PostMapping("/payment")
    public OrderDto payOrder(@RequestBody String orderId) {
        return orderService.payOrder(orderId);
    }

    @PostMapping("/payment/failed")
    public OrderDto handlePaymentError(@RequestBody String orderId) {
        return orderService.handlePaymentError(orderId);
    }

    @PostMapping("/delivery")
    public OrderDto deliveryOrder(@RequestBody String orderId) {
        return orderService.deliveryOrder(orderId);
    }

    @PostMapping("/delivery/failed")
    public OrderDto handleDeliveryFailed(@RequestBody String orderId) {
        return orderService.handleDeliveryFailed(orderId);
    }

    @PostMapping("/completed")
    public OrderDto completeOrder(@RequestBody String orderId) {
        return orderService.completeOrder(orderId);
    }

    @PostMapping("/calculate/total")
    public OrderDto calculateTotal(@RequestBody String orderId) {
        return orderService.calculateTotal(orderId);
    }

    @PostMapping("/calculate/delivery")
    public OrderDto calculateDelivery(@RequestBody String orderId) {
        return orderService.calculateDelivery(orderId);
    }

    @PostMapping("/assembly")
    public OrderDto assembleOrder(@RequestBody String orderId) {
        return orderService.assembleOrder(orderId);
    }

    @PostMapping("/assembly/failed")
    public OrderDto handleAssemblyFailed(@RequestBody String orderId) {
            return orderService.handleAssemblyFailed(orderId);
    }
}
