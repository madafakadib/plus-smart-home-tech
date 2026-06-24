package ru.yandex.practicum.payment.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.api.order.OrderDto;
import ru.yandex.practicum.api.order.PaymentResponseDto;
import ru.yandex.practicum.payment.service.PaymentService;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/v1/payment")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping
    public PaymentResponseDto createPayment(@RequestBody OrderDto orderDto) {
        return paymentService.createPayment(orderDto);
    }

    @PostMapping("/calculate/total")
    public BigDecimal calculateTotalOrderPrice(@RequestBody OrderDto orderDto) {
            return paymentService.calculateTotalOrderPrice(orderDto);
    }

    @PostMapping("/refund")
    public void emulatePaymentSuccess(@RequestBody String paymentId) {
        paymentService.setPaymentSuccess(paymentId);
    }

    @PostMapping("/calculate/productCost")
    public BigDecimal calculateOnlyProductsPrice(@RequestBody OrderDto orderDto) {
        return paymentService.calculateOnlyProductsPrice(orderDto);
    }

    @PostMapping("/failed")
    public void emulatePaymentFailed(@RequestBody String paymentId) {
          paymentService.setPaymentFailed(paymentId);
    }
}
