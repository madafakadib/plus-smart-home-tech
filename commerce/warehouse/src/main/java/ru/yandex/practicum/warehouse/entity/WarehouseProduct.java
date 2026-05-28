package ru.yandex.practicum.warehouse.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "warehouse_products", schema = "warehouse")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WarehouseProduct {

    @Id
    @Column(name = "product_id", nullable = false, updatable = false)
    private String productId;

    @Column(name = "fragile", nullable = false)
    private boolean fragile;

    @Embedded
    private DimensionEmbeddable dimension;

    @Column(name = "weight", nullable = false)
    private Double weight;

    @Builder.Default
    @Column(name = "quantity", nullable = false)
    private Long quantity = 0L;
}
