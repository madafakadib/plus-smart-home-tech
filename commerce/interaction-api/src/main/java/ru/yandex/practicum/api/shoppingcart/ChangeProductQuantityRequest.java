package ru.yandex.practicum.api.shoppingcart;

import lombok.*;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ChangeProductQuantityRequest {
    private String productId;
    private Integer newQuantity;
}
