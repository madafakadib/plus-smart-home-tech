package ru.yandex.practicum.analyzer.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "sensors")
public class Sensor {

    @Id
    String id;

    @Column(name = "hub_id")
    String hubId;
}
