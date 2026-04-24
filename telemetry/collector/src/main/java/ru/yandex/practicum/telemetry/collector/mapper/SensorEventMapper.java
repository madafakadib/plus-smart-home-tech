package ru.yandex.practicum.telemetry.collector.mapper;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.telemetry.collector.sensorEvents.SensorEvent;
import ru.yandex.practicum.telemetry.collector.sensorEvents.LightSensorEvent;
import ru.yandex.practicum.telemetry.collector.sensorEvents.MotionSensorEvent;
import ru.yandex.practicum.telemetry.collector.sensorEvents.SwitchSensorEvent;
import ru.yandex.practicum.telemetry.collector.sensorEvents.ClimateSensorEvent;
import ru.yandex.practicum.telemetry.collector.sensorEvents.TemperatureSensorEvent;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.LightSensorAvro;
import ru.yandex.practicum.kafka.telemetry.event.MotionSensorAvro;
import ru.yandex.practicum.kafka.telemetry.event.SwitchSensorAvro;
import ru.yandex.practicum.kafka.telemetry.event.ClimateSensorAvro;
import ru.yandex.practicum.kafka.telemetry.event.TemperatureSensorAvro;

import java.time.Instant;

@Component
public class SensorEventMapper {

    public SensorEventAvro mapToAvro(SensorEvent sensorEvent) {
        return SensorEventAvro.newBuilder()
                .setId(sensorEvent.getId())
                .setHubId(sensorEvent.getHubId())
                .setTimestamp(sensorEvent.getTimestamp())
                .setPayload(getPayload(sensorEvent))
                .build();
    }

    public SensorEventAvro mapToAvro(SensorEventProto sensorEvent) {
        return SensorEventAvro.newBuilder()
                .setId(sensorEvent.getId())
                .setHubId(sensorEvent.getHubId())
                .setTimestamp(mapTimestamp(sensorEvent.getTimestamp()))
                .setPayload(getPayload(sensorEvent))
                .build();
    }

    private Object getPayload(SensorEvent event) {
        switch (event.getType()) {
            case LIGHT_SENSOR_EVENT -> {
                if (event instanceof LightSensorEvent lightSensorEvent){
                    return LightSensorAvro.newBuilder()
                            .setLinkQuality(lightSensorEvent.getLinkQuality())
                            .setLuminosity(lightSensorEvent.getLuminosity())
                        .build();
                }
            }
            case MOTION_SENSOR_EVENT -> {
                if (event instanceof MotionSensorEvent motionSensorEvent){
                    return MotionSensorAvro.newBuilder()
                            .setLinkQuality(motionSensorEvent.getLinkQuality())
                            .setMotion(motionSensorEvent.isMotion())
                            .setVoltage(motionSensorEvent.getVoltage())
                            .build();
                }
            }
            case SWITCH_SENSOR_EVENT -> {
                if (event instanceof SwitchSensorEvent switchSensorEvent) {
                    return SwitchSensorAvro.newBuilder()
                            .setState(switchSensorEvent.isState())
                            .build();
                }
            }
            case CLIMATE_SENSOR_EVENT -> {
                if (event instanceof ClimateSensorEvent climateSensorEvent) {
                    return ClimateSensorAvro.newBuilder()
                            .setTemperatureC(climateSensorEvent.getTemperatureC())
                            .setHumidity(climateSensorEvent.getHumidity())
                            .setCo2Level(climateSensorEvent.getCo2Level())
                            .build();
                }
            }
            case TEMPERATURE_SENSOR_EVENT -> {
                if (event instanceof TemperatureSensorEvent temperatureSensorEvent) {
                    return TemperatureSensorAvro.newBuilder()
                            .setTemperatureC(temperatureSensorEvent.getTemperatureC())
                            .setTemperatureF(temperatureSensorEvent.getTemperatureF())
                            .build();
                }
            }
            default -> {
                return null;
            }
        }
        return null;
    }

    private Instant mapTimestamp(com.google.protobuf.Timestamp protoTimestamp) {
        return Instant.ofEpochSecond(
                protoTimestamp.getSeconds(),
                protoTimestamp.getNanos()
        );
    }

    private Object getPayload(SensorEventProto event) {
        switch (event.getPayloadCase()) {
            case LIGHT_SENSOR -> {
                var light = event.getLightSensor();
                return LightSensorAvro.newBuilder()
                        .setLinkQuality(light.getLinkQuality())
                        .setLuminosity(light.getLuminosity())
                        .build();
            }
            case MOTION_SENSOR -> {
               var motion = event.getMotionSensor();
               return MotionSensorAvro.newBuilder()
                       .setLinkQuality(motion.getLinkQuality())
                       .setMotion(motion.getMotion())
                       .setVoltage(motion.getVoltage())
                       .build();
            }
            case SWITCH_SENSOR -> {
                var switcher = event.getSwitchSensor();
                return SwitchSensorAvro.newBuilder()
                        .setState(switcher.getState())
                       .build();
            }
            case CLIMATE_SENSOR -> {
                var climate = event.getClimateSensor();
                return ClimateSensorAvro.newBuilder()
                        .setTemperatureC(climate.getTemperatureC())
                        .setHumidity(climate.getHumidity())
                        .setCo2Level(climate.getCo2Level())
                        .build();
            }
            case TEMPERATURE_SENSOR -> {
                var temperature = event.getTemperatureSensor();
                return TemperatureSensorAvro.newBuilder()
                        .setTemperatureC(temperature.getTemperatureC())
                        .setTemperatureF(temperature.getTemperatureF())
                        .build();

            }
       }
        return null;
    }
}
