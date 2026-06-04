package ru.yandex.practicum.api.warehouse;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter @Setter
@Builder
@AllArgsConstructor @NoArgsConstructor
public class AddressDto {

    @NotNull(message = "Поле 'страна' не может быть null")
    private String country;

    @NotNull(message = "Поле 'город' не может быть null")
    private String city;

    @NotNull(message = "Поле 'улица' не может быть null")
    private String street;

    @NotNull(message = "Поле 'дом' не может быть null")
    private String house;

    @NotNull(message = "Поле 'квартира' не может быть null")
    private String flat;

}
