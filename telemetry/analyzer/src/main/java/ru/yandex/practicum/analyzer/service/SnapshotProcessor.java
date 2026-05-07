package ru.yandex.practicum.analyzer.service;

import com.google.protobuf.util.Timestamps;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.analyzer.entity.Condition;
import ru.yandex.practicum.analyzer.entity.Scenario;
import ru.yandex.practicum.analyzer.entity.ScenarioAction;
import ru.yandex.practicum.analyzer.repository.ScenarioRepository;
import ru.yandex.practicum.grpc.telemetry.event.ActionTypeProto;
import ru.yandex.practicum.grpc.telemetry.event.DeviceActionProto;
import ru.yandex.practicum.grpc.telemetry.event.DeviceActionRequest;
import ru.yandex.practicum.grpc.telemetry.hubrouter.HubRouterControllerGrpc;
import ru.yandex.practicum.kafka.telemetry.event.*;

import java.time.Duration;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class SnapshotProcessor{

    private final KafkaConsumer<String, SensorsSnapshotAvro> consumer;
    private final ScenarioRepository scenarioRepository;
    private final HubRouterControllerGrpc.HubRouterControllerBlockingStub hubRouterControllerBlockingStub;

    public SnapshotProcessor(@Qualifier("snapshotConsumer") KafkaConsumer<String, SensorsSnapshotAvro> consumer,
                             ScenarioRepository scenarioRepository,
                             @GrpcClient("hub-router")HubRouterControllerGrpc.HubRouterControllerBlockingStub hubRouterControllerBlockingStub) {
        this.consumer = consumer;
        this.scenarioRepository = scenarioRepository;
        this.hubRouterControllerBlockingStub = hubRouterControllerBlockingStub;
    }

    public void start() {
        try {
            consumer.subscribe(List.of("telemetry.snapshots.v1"));
            while (!Thread.currentThread().isInterrupted()) {
                ConsumerRecords<String, SensorsSnapshotAvro> records =
                        consumer.poll(Duration.ofMillis(5000));
                for (ConsumerRecord<String, SensorsSnapshotAvro> record : records) {
                    processSnapshot(record.value());
                }
                consumer.commitSync();
            }
        } finally {
            consumer.close();
        }
    }

    private void processSnapshot(SensorsSnapshotAvro snapshot) {
        String hubId = snapshot.getHubId();
        List<Scenario> scenarios = scenarioRepository.findByHubId(hubId);


        for (Scenario scenario : scenarios) {
            if (checkConditions(scenario, snapshot)) {
                executeActions(scenario);
            }
        }
    }

    private boolean checkConditions(Scenario scenario, SensorsSnapshotAvro snapshot) {
        Map<String, SensorStateAvro> sensorStates = snapshot.getSensorsState();

        return scenario.getConditions().stream().allMatch(sc -> {
            String sensorId = sc.getSensor().getId();
            Condition condition = sc.getCondition();

            SensorStateAvro state = sensorStates.get(sensorId);
            if (state == null) return false;

            Integer currentValue = getSensorValue(state.getData(), condition.getType());
            log.info("Датчик: {}, Тип условия: {}, Текущее значение: {}, Ожидаемое: {} {}",
                    sensorId, condition.getType(), currentValue, condition.getOperation(), condition.getValue());

            return checkCondition(condition, currentValue);
        });
    }


    private boolean checkCondition(Condition condition, Integer sensorValue) {
        if (sensorValue == null) return false;

        return switch (condition.getOperation().toUpperCase()) {
            case "EQUALS", "0" -> sensorValue.equals(condition.getValue());
            case "GREATER_THAN", "1" -> sensorValue > condition.getValue();
            case "LOWER_THAN", "2" -> sensorValue < condition.getValue();
            default -> false;
        };
    }

    private Integer getSensorValue(Object payload, String conditionType) {

        if (payload instanceof ClimateSensorAvro climate) {
            return switch (conditionType.toUpperCase()) {
                case "TEMPERATURE" -> climate.getTemperatureC();
                case "HUMIDITY" -> climate.getHumidity();
                case "CO2_LEVEL" -> climate.getCo2Level();
                default -> null;
            };
        }

        if (payload instanceof LightSensorAvro light) {
            return "LUMINOSITY".equalsIgnoreCase(conditionType) ? light.getLuminosity() : null;
        }

        if (payload instanceof MotionSensorAvro motion) {
            return "MOTION".equalsIgnoreCase(conditionType) ? (motion.getMotion() ? 1 : 0) : null;
        }

        if (payload instanceof SwitchSensorAvro sw) {
            return ("STATE".equalsIgnoreCase(conditionType) || "SWITCH".equalsIgnoreCase(conditionType))
                    ? (sw.getState() ? 1 : 0) : null;
        }

        if (payload instanceof TemperatureSensorAvro temp) {
            return "TEMPERATURE".equalsIgnoreCase(conditionType) ? temp.getTemperatureC() : null;
        }

        return null;
    }

    private void executeActions(Scenario scenario) {
        for (ScenarioAction sa : scenario.getActions()) {
            DeviceActionProto.Builder actionBuilder = DeviceActionProto.newBuilder()
                    .setSensorId(sa.getSensor().getId())
                    .setType(ActionTypeProto.valueOf(sa.getAction().getType()));

            if (sa.getAction().getValue() != null) {
                actionBuilder.setValue(sa.getAction().getValue());
            }

            DeviceActionRequest request = DeviceActionRequest.newBuilder()
                    .setHubId(scenario.getHubId())
                    .setScenarioName(scenario.getName())
                    .setAction(actionBuilder.build())
                    .setTimestamp(Timestamps.fromMillis(System.currentTimeMillis()))
                    .build();

            try {
                hubRouterControllerBlockingStub.handleDeviceAction(request);
                log.info("Команда отправлена: сценарий {}, устройство {}", scenario.getName(), sa.getSensor().getId());
            } catch (Exception e) {
                log.error("Ошибка при отправке команды gRPC", e);
            }
        }
    }

}
