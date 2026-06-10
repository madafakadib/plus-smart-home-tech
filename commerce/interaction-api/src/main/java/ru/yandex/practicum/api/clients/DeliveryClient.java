package ru.yandex.practicum.api.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.api.delivery.DeliveryDto;
import ru.yandex.practicum.api.order.OrderDto;

import java.math.BigDecimal;

@FeignClient(name = "delivery", path = "/api/v1/delivery")
public interface DeliveryClient {

    @PutMapping
    DeliveryDto planDelivery(@RequestBody DeliveryDto deliveryDto);

    @PostMapping("/cost")
    BigDecimal deliveryCost(@RequestBody OrderDto orderDto);
}
