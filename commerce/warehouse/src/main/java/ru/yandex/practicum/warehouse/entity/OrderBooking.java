package ru.yandex.practicum.warehouse.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "order_bookings", schema = "warehouse")
@Getter @Setter
@Builder
@AllArgsConstructor @NoArgsConstructor
public class OrderBooking {

    @Id
    private String id;

    @Column(name = "order_id", nullable = false)
    private String orderId;

    @Column(name = "product_id", nullable = false)
    private String productId;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    @Column(name = "delivery_id")
    private String deliveryId;
}
