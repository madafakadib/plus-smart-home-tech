package ru.yandex.practicum.api.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.api.order.OrderDto;
import ru.yandex.practicum.api.order.PaymentResponseDto;

import java.math.BigDecimal;

@FeignClient(name = "payment", path = "/api/v1/payment")
public interface PaymentClient {

    @PostMapping("/calculate/products")
    BigDecimal productCost(@RequestBody OrderDto orderDto);

    @PostMapping("/calculate/total")
    BigDecimal getTotalCost(@RequestBody OrderDto orderDto);

    @PostMapping
    PaymentResponseDto payment(@RequestBody OrderDto orderDto);
}
