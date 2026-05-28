package ru.yandex.practicum.api.shoppingstore;

import lombok.*;

import java.util.UUID;

@Builder @Setter @Getter
@NoArgsConstructor @AllArgsConstructor
public class SetProductQuantityStateRequest {
    private String productId;
    private QuantityState quantityState;
}
