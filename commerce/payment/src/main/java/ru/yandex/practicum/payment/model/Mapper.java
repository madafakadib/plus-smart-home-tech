package ru.yandex.practicum.payment.model;

import ru.yandex.practicum.api.order.OrderDto;
import ru.yandex.practicum.api.order.PaymentResponseDto;

import java.math.BigDecimal;
import java.util.UUID;

public class Mapper {

    public static Payment toEntity(OrderDto orderDto, BigDecimal productPrice, BigDecimal taxPrice, BigDecimal deliveryPrice, BigDecimal totalPrice) {
        if (orderDto == null) {
            return null;
        }

        Payment payment = new Payment();
        payment.setId(UUID.randomUUID().toString());
        payment.setOrderId(orderDto.getOrderId() != null ? orderDto.getOrderId().replace("\"", "").trim() : null);
        payment.setProductPrice(productPrice);
        payment.setDeliveryPrice(deliveryPrice);
        payment.setTaxPrice(taxPrice);
        payment.setTotalPrice(totalPrice);
        payment.setStatus(PaymentStatus.PENDING);

        return payment;
    }

    public static PaymentResponseDto toResponseDto(Payment payment) {
        return new PaymentResponseDto(
                payment.getId(),
                payment.getTotalPrice(),
                payment.getDeliveryPrice(),
                payment.getTaxPrice()
        );
    }
}
