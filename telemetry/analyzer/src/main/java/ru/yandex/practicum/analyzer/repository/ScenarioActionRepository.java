package ru.yandex.practicum.analyzer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.analyzer.entity.ScenarioAction;

public interface ScenarioActionRepository extends JpaRepository<ScenarioAction, Long> {
}
