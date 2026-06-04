package ru.yandex.practicum.analyzer.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "conditions")
public class Condition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "type")
    String type;

    @Column(name = "operation")
    String operation;

    @Column(name = "value")
    Integer value;
}
