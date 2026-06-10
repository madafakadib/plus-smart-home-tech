package ru.yandex.practicum.api.delivery;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.api.warehouse.AddressDto;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryDto {
    private String deliveryId;
    private AddressDto fromAddress;
    private AddressDto toAddress;
    private String orderId;
    private String deliveryState;
}
