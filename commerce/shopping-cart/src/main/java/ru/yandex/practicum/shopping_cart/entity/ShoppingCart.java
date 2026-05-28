package ru.yandex.practicum.shopping_cart.entity;

import java.util.List;

import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;

@Entity
@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
@Table(name = "shopping_carts", schema = "shoppingcart")
public class ShoppingCart {

    @Id
    @Column(name = "shopping_cart_id")
    private String shoppingCartId;

    @OneToMany(mappedBy = "shoppingCart", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<CartItem> items = new ArrayList<>();

    @Column(name = "username", nullable = false, unique = true)
    private String username;

}
