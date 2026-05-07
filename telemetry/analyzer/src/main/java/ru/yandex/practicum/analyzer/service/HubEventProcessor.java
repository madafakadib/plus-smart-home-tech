package ru.yandex.practicum.analyzer.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.analyzer.entity.*;
import ru.yandex.practicum.analyzer.repository.*;
import ru.yandex.practicum.kafka.telemetry.event.*;

import java.time.Duration;
import java.util.List;

@Slf4j
@Component
public class HubEventProcessor implements Runnable {
    private final KafkaConsumer<String, HubEventAvro> consumer;
    private final ScenarioRepository scenarioRepository;
    private final SensorRepository sensorRepository;
    private final ConditionRepository conditionRepository;
    private final ScenarioConditionRepository scenarioConditionRepository;
    private final ActionRepository actionRepository;
    private final ScenarioActionRepository scenarioActionRepository;

    public HubEventProcessor(@Qualifier("hubEventConsumer") KafkaConsumer<String, HubEventAvro> consumer,
                             ScenarioRepository scenarioRepository,
                             SensorRepository sensorRepository, ConditionRepository conditionRepository, ScenarioConditionRepository scenarioConditionRepository, ActionRepository actionRepository, ScenarioActionRepository scenarioActionRepository) {
        this.consumer = consumer;
        this.scenarioRepository = scenarioRepository;
        this.sensorRepository = sensorRepository;
        this.conditionRepository = conditionRepository;
        this.scenarioConditionRepository = scenarioConditionRepository;
        this.actionRepository = actionRepository;
        this.scenarioActionRepository = scenarioActionRepository;
    }

    @Override
    public void run() {
        try {
            consumer.subscribe(List.of("telemetry.hubs.v1"));
            while (!Thread.currentThread().isInterrupted()) {
                ConsumerRecords<String, HubEventAvro> records = consumer.poll(Duration.ofMillis(5000));
                for (ConsumerRecord<String, HubEventAvro> record : records) {
                    try {
                        log.info("Получено событие хаба: {}", record.value());
                        processEvent(record.value());
                    } catch (Exception e) {
                        log.error("Ошибка при обработке записи из Kafka", e);
                    }
                }
                consumer.commitSync();
            }


        } catch (Exception e) {
            log.error("Критическая ошибка в HubEventProcessor", e);
        } finally {
            consumer.close();
        }

    }

    @Transactional
    private void processEvent(HubEventAvro event) {
        if (event.getPayload() instanceof DeviceAddedEventAvro deviceAdded) {
            Sensor sensor = new Sensor();
            sensor.setId(deviceAdded.getId());
            sensor.setHubId(event.getHubId());
            sensorRepository.save(sensor);
        }
        if (event.getPayload() instanceof DeviceRemovedEventAvro deviceRemoved) {
            sensorRepository.deleteById(deviceRemoved.getId());
        }
        if (event.getPayload() instanceof ScenarioAddedEventAvro scenarioAdded) {
            Scenario scenario = new Scenario();
            scenario.setHubId(event.getHubId());
            scenario.setName(scenarioAdded.getName());
            scenario = scenarioRepository.save(scenario);

            for (ScenarioConditionAvro condAvro : scenarioAdded.getConditions()) {
                Condition condition = new Condition();
                condition.setType(condAvro.getType().name());
                condition.setOperation(condAvro.getOperation().name());
                Object val = condAvro.getValue();
                if (val instanceof Boolean) {
                    condition.setValue((Boolean) val ? 1 : 0);
                } else if (val instanceof Integer) {
                    condition.setValue((Integer) val);
                }
                condition = conditionRepository.save(condition);

                Sensor sensor = sensorRepository.findById(condAvro.getSensorId())
                        .orElseThrow(() -> new RuntimeException("Sensor not found"));

                ScenarioCondition sc = new ScenarioCondition();
                sc.setScenario(scenario);
                sc.setSensor(sensor);
                sc.setCondition(condition);
                scenarioConditionRepository.save(sc);
            }

            for (DeviceActionAvro actionAvro : scenarioAdded.getActions()) {
                Action action = new Action();
                action.setType(actionAvro.getType().name());
                if (actionAvro.getValue() != null) {
                    action.setValue(actionAvro.getValue());
                } else {
                    if ("ACTIVATE".equalsIgnoreCase(actionAvro.getType().name())) {
                        action.setValue(1);
                    } else {
                        action.setValue(0);
                    }
                }
                action = actionRepository.save(action);

                Sensor sensor = sensorRepository.findById(actionAvro.getSensorId())
                        .orElseThrow(() -> new RuntimeException("Sensor not found"));

                ScenarioAction sa = new ScenarioAction();
                sa.setScenario(scenario);
                sa.setSensor(sensor);
                sa.setAction(action);
                scenarioActionRepository.save(sa);
            }
        }
        if (event.getPayload() instanceof ScenarioRemovedEventAvro scenarioRemoved) {
            scenarioRepository.findByHubIdAndName(event.getHubId(), scenarioRemoved.getName()).ifPresent(scenarioRepository::delete);
        }
    }
}