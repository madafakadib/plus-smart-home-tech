package mapper;

import org.springframework.stereotype.Component;
import sensorEvents.SensorEvent;
import sensorEvents.LightSensorEvent;
import sensorEvents.MotionSensorEvent;
import sensorEvents.SwitchSensorEvent;
import sensorEvents.ClimateSensorEvent;
import sensorEvents.TemperatureSensorEvent;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.LightSensorAvro;
import ru.yandex.practicum.kafka.telemetry.event.MotionSensorAvro;
import ru.yandex.practicum.kafka.telemetry.event.SwitchSensorAvro;
import ru.yandex.practicum.kafka.telemetry.event.ClimateSensorAvro;
import ru.yandex.practicum.kafka.telemetry.event.TemperatureSensorAvro;

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
}
