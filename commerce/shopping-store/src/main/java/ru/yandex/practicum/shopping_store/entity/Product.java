package ru.yandex.practicum.shopping_store.entity;

import jakarta.persistence.*;
import lombok.*;
import ru.yandex.practicum.api.shoppingstore.QuantityState;
import ru.yandex.practicum.api.shoppingstore.ProductState;
import ru.yandex.practicum.api.shoppingstore.ProductCategory;

import java.math.BigDecimal;

@Entity
@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
@Table(name = "products", schema = "shoppingstore")
public class Product {

    @Id
    @Column(name = "product_id")
    String productId;

    @Column(name = "product_name", nullable = false)
    String productName;

    @Column(name = "description", nullable = false)
    String description;

    @Column(name = "image_src")
    String imageSrc;

    @Column(name = "quantity_state", nullable = false)
    @Enumerated(EnumType.STRING)
    QuantityState quantityState;

    @Column(name = "product_state", nullable = false)
    @Enumerated(EnumType.STRING)
    ProductState productState;

    @Column(name = "product_category")
    @Enumerated(EnumType.STRING)
    ProductCategory productCategory;

    @Column(name = "price", nullable = false)
    BigDecimal price;

}
