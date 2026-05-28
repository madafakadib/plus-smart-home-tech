package ru.yandex.practicum.api.warehouse;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter @Setter
@Builder
@AllArgsConstructor @NoArgsConstructor
public class AddProductToWarehouseRequest {

    @NotNull(message = "Идентификатор товара обязателен")
    private String productId;

    @NotNull(message = "Количество товара обязательно")
    @Min(value = 1, message = "Количество товара для добавления должно быть не менее 1")
    private Long quantity;

}
