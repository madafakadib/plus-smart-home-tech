package ru.yandex.practicum.delivery.model;

import ru.yandex.practicum.api.delivery.DeliveryDto;
import ru.yandex.practicum.api.warehouse.AddressDto;

import java.util.UUID;

public class Mapper {

    public static Delivery toEntity(DeliveryDto dto) {
        if (dto == null) return null;

        Delivery delivery = new Delivery();
        delivery.setId(dto.getDeliveryId() != null && !dto.getDeliveryId().isBlank()
                ? dto.getDeliveryId().replace("\"", "").trim()
                : UUID.randomUUID().toString());

        delivery.setOrderId(dto.getOrderId() != null ? dto.getOrderId().replace("\"", "").trim() : null);
        delivery.setFromAddress(toAddressEntity(dto.getFromAddress()));
        delivery.setToAddress(toAddressEntity(dto.getToAddress()));

        delivery.setTotalVolume(0.0);
        delivery.setTotalWeight(0.0);
        delivery.setFragile(false);

        delivery.setStatus(DeliveryStatus.CREATED);
        return delivery;
    }

    public static DeliveryDto toDto(Delivery delivery) {
        if (delivery == null) return null;

        return new DeliveryDto(
                delivery.getId(),
                toAddressDto(delivery.getFromAddress()),
                toAddressDto(delivery.getToAddress()),
                delivery.getOrderId(),
                delivery.getStatus() != null ? delivery.getStatus().name() : null
        );
    }

    private static Address toAddressEntity(AddressDto dto) {
        if (dto == null) return null;
        return new Address(dto.getCountry(), dto.getCity(), dto.getStreet(), dto.getHouse(), dto.getFlat());
    }

    private static AddressDto toAddressDto(Address entity) {
        if (entity == null) return null;
        return AddressDto.builder()
                .country(entity.getCountry())
                .city(entity.getCity())
                .street(entity.getStreet())
                .house(entity.getHouse())
                .flat(entity.getFlat())
                .build();
    }
}
