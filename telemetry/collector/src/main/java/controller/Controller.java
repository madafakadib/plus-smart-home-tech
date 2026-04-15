package controller;

import hubEvents.HubEvent;
import mapper.HubEventMapper;
import mapper.SensorEventMapper;
import org.springframework.web.bind.annotation.RequestMapping;
import sensorEvents.SensorEvent;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import service.EventProducer;

@Validated
@RestController
@RequestMapping("/events")
public class Controller {

    private final EventProducer eventProducer;
    private final HubEventMapper hubEventMapper;
    private final SensorEventMapper sensorEventMapper;

    public Controller(EventProducer eventProducer, HubEventMapper hubEventMapper, SensorEventMapper sensorEventMapper) {
        this.eventProducer = eventProducer;
        this.hubEventMapper = hubEventMapper;
        this.sensorEventMapper = sensorEventMapper;
    }

    @PostMapping("/sensors")
    public void collectSensorEvent(@Valid @RequestBody SensorEvent event) {
        eventProducer.send("telemetry.sensors.v1", event.getHubId(), sensorEventMapper.mapToAvro(event));
    }

    @PostMapping("/hubs")
    public void collectHubsEvent(@Valid @RequestBody HubEvent event) {
        eventProducer.send("telemetry.hubs.v1", event.getHubId(), hubEventMapper.mapToAvro(event));
    }

}
