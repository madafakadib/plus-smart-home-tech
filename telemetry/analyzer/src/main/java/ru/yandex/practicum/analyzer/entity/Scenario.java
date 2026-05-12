package ru.yandex.practicum.analyzer.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "scenarios")
public class Scenario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "hub_id")
    private String hubId;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "scenario", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<ScenarioCondition> conditions;

    @OneToMany(mappedBy = "scenario", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<ScenarioAction> actions;
}
