package ru.yandex.practicum.telemetry.collector.service;

import jakarta.annotation.PreDestroy;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.stereotype.Service;

@Service
public class EventProducer {

    private final Producer<String, SpecificRecordBase> producer;

    public EventProducer(Producer<String, SpecificRecordBase> producer) {
        this.producer = producer;
    }

    public void send(String topic, String key, SpecificRecordBase event) {
        ProducerRecord<String, SpecificRecordBase> record = new ProducerRecord<>(topic, key, event);
        try {
            producer.send(record).get();
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при синхронной отправке в Kafka", e);
        }
    }

    @PreDestroy
    public void stop() {
        producer.flush();
        producer.close();
    }
}
