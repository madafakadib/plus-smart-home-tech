package ru.yandex.practicum.api.warehouse;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter @Setter
@Builder
@AllArgsConstructor @NoArgsConstructor
public class BookedProductsDto {

    @NotNull(message = "Общий вес доставки обязателен")
    private Double deliveryWeight;

    @NotNull(message = "Общий объем доставки обязателен")
    private Double deliveryVolume;

    @NotNull(message = "Признак хрупкости обязателен")
    private Boolean fragile;

}
