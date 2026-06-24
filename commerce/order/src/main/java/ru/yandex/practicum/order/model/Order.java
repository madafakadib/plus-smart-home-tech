package ru.yandex.practicum.order.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders", schema = "orders")
@Getter
@Setter
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderState state;

    @Column(name = "shopping_cart_id")
    private String shoppingCartId;

    @Column(name = "delivery_id")
    private String deliveryId;

    @Column(name = "payment_id")
    private String paymentId;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "order", fetch = FetchType.LAZY)
    private List<OrderItem> items = new ArrayList<>();

    @Column(name = "total_volume")
    private Double totalVolume;

    @Column(name = "total_weight")
    private Double totalWeight;

    private Boolean fragile;

    @Column(name = "total_price", precision = 10, scale = 2)
    private BigDecimal totalPrice;

    @Column(name = "items_price", precision = 10, scale = 2)
    private BigDecimal itemsPrice;

    @Column(name = "delivery_price", precision = 10, scale = 2)
    private BigDecimal deliveryPrice;
}
