package ru.yandex.practicum.analyzer.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "scenario_conditions")
@IdClass(ScenarioConditionId.class)
public class ScenarioCondition {
    @Id
    @ManyToOne
    @JoinColumn(name = "scenario_id")
    private Scenario scenario;

    @Id
    @ManyToOne
    @JoinColumn(name = "sensor_id")
    private Sensor sensor;

    @Id
    @OneToOne
    @JoinColumn(name = "condition_id")
    private Condition condition;
}
