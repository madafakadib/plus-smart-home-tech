package mapper;

import hubEvents.*;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.*;
import sensorEvents.*;

@Component
public class HubEventMapper {
    public HubEventAvro mapToAvro(HubEvent hubEvent) {
        return HubEventAvro.newBuilder()
                .setHubId(hubEvent.getHubId())
                .setTimestamp(hubEvent.getTimestamp())
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
}
