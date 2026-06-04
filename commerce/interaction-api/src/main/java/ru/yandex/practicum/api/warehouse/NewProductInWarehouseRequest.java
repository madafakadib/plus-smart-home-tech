package ru.yandex.practicum.api.warehouse;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter @Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NewProductInWarehouseRequest {

    @NotNull(message = "Идентификатор товара обязателен")
    private String productId;

    private boolean fragile;

    @NotNull(message = "Размеры товара обязательны")
    @Valid
    private DimensionDto dimension;

    @NotNull(message = "Вес товара обязателен")
    @Min(value = 1, message = "Вес товара должен быть не менее 1")
    private Double weight;
}

