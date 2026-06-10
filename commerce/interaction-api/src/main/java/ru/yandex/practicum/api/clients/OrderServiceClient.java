package ru.yandex.practicum.api.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "order", path = "/api/v1/order")
public interface OrderServiceClient {

    @PostMapping("/payment")
    void notifyPaymentSuccess(@RequestBody String orderId);

    @PostMapping("/{orderId}/assembled")
    void notifyOrderAssembled(@PathVariable("orderId") String orderId);

    @PostMapping("/delivery/failed")
    void notifyDeliveryFailed(@RequestBody String orderId);

    @PostMapping("/payment/failed")
    void notifyPaymentFailed(@RequestBody String orderId);
}
