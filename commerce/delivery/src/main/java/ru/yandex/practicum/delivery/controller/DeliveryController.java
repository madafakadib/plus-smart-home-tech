package ru.yandex.practicum.delivery.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.api.delivery.DeliveryDto;
import ru.yandex.practicum.delivery.service.DeliveryService;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/v1/delivery")
public class DeliveryController {
    private final DeliveryService deliveryService;

    public DeliveryController(DeliveryService deliveryService) {
        this.deliveryService = deliveryService;
    }

    @PutMapping
    public DeliveryDto createDelivery(@RequestBody DeliveryDto deliveryDto) {
        return deliveryService.createDelivery(deliveryDto);
    }

    @PostMapping("/successful")
    public DeliveryDto emulateDeliverySuccess(@RequestBody String orderId) {
        return deliveryService.emulateDeliverySuccess(orderId);
    }

    @PostMapping("/picked")
    public DeliveryDto emulateAcceptDelivery(@RequestBody String orderId) {
        return deliveryService.emulateAcceptDelivery(orderId);
    }

    @PostMapping("/failed")
    public DeliveryDto emulateDeliveryFailed(@RequestBody String orderId) {
        return deliveryService.emulateDeliveryFailed(orderId);
    }

    @PostMapping("/cost")
    public BigDecimal calculateDeliveryCost(@RequestBody ru.yandex.practicum.api.order.OrderDto orderDto) {
        return deliveryService.calculateDeliveryCost(orderDto);
    }


}
