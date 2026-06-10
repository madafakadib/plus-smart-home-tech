package ru.yandex.practicum.api.delivery;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.api.warehouse.AddressDto;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateDeliveryRequest {
    private String orderId;
    private double totalVolume;
    private double totalWeight;
    private boolean fragile;
    private AddressDto fromAddress;
    private AddressDto toAddress;
}
