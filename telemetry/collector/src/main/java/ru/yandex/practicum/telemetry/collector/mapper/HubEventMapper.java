package ru.yandex.practicum.telemetry.collector.mapper;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.grpc.telemetry.event.DeviceActionProto;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.grpc.telemetry.event.ScenarioConditionProto;
import ru.yandex.practicum.kafka.telemetry.event.*;
import ru.yandex.practicum.telemetry.collector.hubEvents.*;

import java.time.Instant;

@Component
public class HubEventMapper {
    public HubEventAvro mapToAvro(HubEvent hubEvent) {
        return HubEventAvro.newBuilder()
                .setHubId(hubEvent.getHubId())
                .setTimestamp(hubEvent.getTimestamp())
                .setPayload(getPayload(hubEvent))
                .build();
    }

    public HubEventAvro mapToAvro(HubEventProto hubEvent) {
        return HubEventAvro.newBuilder()
                .setHubId(hubEvent.getHubId())
                .setTimestamp(mapToInstant(hubEvent.getTimestamp()))
                .setPayload(getPayload(hubEvent))
                .build();
    }

    private Object getPayload(HubEvent hubEvent) {
        switch (hubEvent.getType()) {
            case DEVICE_ADDED -> {
                if (hubEvent instanceof DeviceAddedEvent deviceAddedEvent){
                    return DeviceAddedEventAvro.newBuilder()
                            .setId(deviceAddedEvent.getId())
                            .setType(DeviceTypeAvro.valueOf(deviceAddedEvent.getDeviceType().name()))
                            .build();
                }
            }
            case DEVICE_REMOVED -> {
                if (hubEvent instanceof DeviceRemovedEvent deviceRemovedEvent){
                    return DeviceRemovedEventAvro.newBuilder()
                            .setId(deviceRemovedEvent.getId())
                            .build();
                }
            }
            case SCENARIO_ADDED -> {
                if (hubEvent instanceof ScenarioAddedEvent scenarioAddedEvent) {
                    return ScenarioAddedEventAvro.newBuilder()
                            .setName(scenarioAddedEvent.getName())
                            .setConditions(scenarioAddedEvent.getConditions().stream().map(this::mapCondition).toList())
                            .setActions(scenarioAddedEvent.getActions().stream().map(this::mapAction).toList())
                            .build();
                }
            }
            case SCENARIO_REMOVED -> {
                if (hubEvent instanceof ScenarioRemovedEvent scenarioRemovedEvent) {
                    return ScenarioRemovedEventAvro.newBuilder()
                            .setName(scenarioRemovedEvent.getName())
                            .build();
                }
            }
            default -> {
                return null;
            }
        }
        return null;
    }

    private ScenarioConditionAvro mapCondition(ScenarioCondition scenarioCondition) {
        return ScenarioConditionAvro.newBuilder()
                .setSensorId(scenarioCondition.getSensorId())
                .setType(ConditionTypeAvro.valueOf(scenarioCondition.getType().name()))
                .setOperation(ConditionOperationAvro.valueOf(scenarioCondition.getOperation().name()))
                .setValue(scenarioCondition.getValue())
                .build();
    }

    private DeviceActionAvro mapAction(DeviceAction deviceAction) {
        return DeviceActionAvro.newBuilder()
                .setSensorId(deviceAction.getSensorId())
                .setType(ActionTypeAvro.valueOf(deviceAction.getType().name()))
                .setValue(deviceAction.getValue())
                .build();
    }

    private Instant mapToInstant(com.google.protobuf.Timestamp protoTimestamp) {
        return Instant.ofEpochSecond(
                protoTimestamp.getSeconds(),
                protoTimestamp.getNanos()
        );
    }

    private Object getPayload(HubEventProto hubEvent) {
        switch (hubEvent.getPayloadCase()) {
            case DEVICE_ADDED -> {
                var deviceAdded = hubEvent.getDeviceAdded();
                return DeviceAddedEventAvro.newBuilder()
                        .setId(deviceAdded.getId())
                        .setType(DeviceTypeAvro.valueOf(deviceAdded.getType().name()))
                        .build();
            }
            case DEVICE_REMOVED -> {
                var deviceRemoved = hubEvent.getDeviceRemoved();
                return DeviceRemovedEventAvro.newBuilder()
                        .setId(deviceRemoved.getId())
                        .build();
            }
            case SCENARIO_REMOVED -> {
                var scenarioRemoved = hubEvent.getScenarioRemoved();
                return ScenarioRemovedEventAvro.newBuilder()
                        .setName(scenarioRemoved.getName())
                        .build();
            }
            case SCENARIO_ADDED -> {
                var scenarioAdded = hubEvent.getScenarioAdded();
                return ScenarioAddedEventAvro.newBuilder()
                        .setName(scenarioAdded.getName())
                        .setConditions(scenarioAdded.getConditionList().stream().map(this::mapCondition).toList())
                        .setActions(scenarioAdded.getActionList().stream().map(this::mapAction).toList())
                        .build();
            }
        }
        return null;
    }

    private ScenarioConditionAvro mapCondition(ScenarioConditionProto scenarioCondition) {
        var scenarioConditionAvro = ScenarioConditionAvro.newBuilder()
                .setSensorId(scenarioCondition.getSensorId())
                .setType(ConditionTypeAvro.valueOf(scenarioCondition.getType().name()))
                .setOperation(ConditionOperationAvro.valueOf(scenarioCondition.getOperation().name()));
        if (scenarioCondition.hasIntValue()) {
            scenarioConditionAvro.setValue(scenarioCondition.getIntValue());
        } else if (scenarioCondition.hasBoolValue()) {
            scenarioConditionAvro.setValue(scenarioCondition.getBoolValue());
        }
        return scenarioConditionAvro.build();
    }

    private DeviceActionAvro mapAction(DeviceActionProto deviceAction) {
        return DeviceActionAvro.newBuilder()
                .setSensorId(deviceAction.getSensorId())
                .setType(ActionTypeAvro.valueOf(deviceAction.getType().name()))
                .setValue(deviceAction.getValue())
                .build();
    }
}