package ru.yandex.practicum.api.payment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentDto {
    private String id;
    private String orderId;
    private BigDecimal productPrice;
    private BigDecimal deliveryPrice;
    private BigDecimal taxPrice;
    private BigDecimal totalPrice;
    private String status;
}
