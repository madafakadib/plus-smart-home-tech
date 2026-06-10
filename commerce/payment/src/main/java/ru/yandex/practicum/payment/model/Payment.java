package ru.yandex.practicum.payment.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

@Entity
@Table(name = "payments", schema = "payments")
@Getter
@Setter
public class Payment {

    @Id
    private String id;

    private String orderId;
    private BigDecimal productPrice;
    private BigDecimal deliveryPrice;
    private BigDecimal taxPrice;
    private BigDecimal totalPrice;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;
}
