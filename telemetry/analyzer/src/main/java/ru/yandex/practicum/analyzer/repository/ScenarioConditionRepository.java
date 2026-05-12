package ru.yandex.practicum.analyzer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.analyzer.entity.ScenarioCondition;

public interface ScenarioConditionRepository extends JpaRepository<ScenarioCondition, Long> {
}
