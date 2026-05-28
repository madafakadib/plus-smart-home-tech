package ru.yandex.practicum.api.warehouse;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter @Setter
@Builder
@AllArgsConstructor @NoArgsConstructor
public class DimensionDto {

    @NotNull(message = "Ширина обязательна для заполнения")
    @Min(value = 1, message = "Ширина должна быть не менее 1")
    private Double width;

    @NotNull(message = "Высота обязательна для заполнения")
    @Min(value = 1, message = "Высота должна быть не менее 1")
    private Double height;

    @NotNull(message = "Глубина обязательна для заполнения")
    @Min(value = 1, message = "Глубина должна быть не менее 1")
    private Double depth;

}
