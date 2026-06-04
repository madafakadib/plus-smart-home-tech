package ru.yandex.practicum.analyzer.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "scenario_actions")
@IdClass(ScenarioActionId.class)
public class ScenarioAction {

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
    @JoinColumn(name = "action_id")
    private Action action;
}