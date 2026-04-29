package ru.yandex.practicum.aggregator;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorStateAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class SnapshotService {

    Map<String, SensorsSnapshotAvro> snapshots = new HashMap<>();

    Optional<SensorsSnapshotAvro> updateState(SensorEventAvro event) {

        SensorsSnapshotAvro currentSnapshot;

        if (snapshots.containsKey(event.getHubId())) {
            currentSnapshot = snapshots.get(event.getHubId());

        } else {
           currentSnapshot = SensorsSnapshotAvro.newBuilder()
                    .setHubId(event.getHubId())
                    .setSensorsState(new HashMap<>())
                    .setTimestamp(event.getTimestamp())
                    .build();

           snapshots.put(event.getHubId(), currentSnapshot);
        }

        SensorStateAvro oldState = currentSnapshot.getSensorsState().get(event.getId());

        if (oldState != null) {
            if (oldState.getTimestamp().isAfter(event.getTimestamp()) ||
                    oldState.getData().equals(event.getPayload())) {
                return Optional.empty();
            }
        }

        SensorStateAvro stateAvro = SensorStateAvro.newBuilder()
                .setTimestamp(event.getTimestamp())
                .setData(event.getPayload())
                .build();
        currentSnapshot.getSensorsState().put(event.getId(), stateAvro);
        currentSnapshot.setTimestamp(stateAvro.getTimestamp());
        return Optional.of(currentSnapshot);
    }
}
