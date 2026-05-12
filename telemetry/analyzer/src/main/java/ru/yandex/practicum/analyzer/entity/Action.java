package ru.yandex.practicum.analyzer.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "action")
public class Action {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "type")
    String type;

    @Column(name = "value")
    Integer value;
}
