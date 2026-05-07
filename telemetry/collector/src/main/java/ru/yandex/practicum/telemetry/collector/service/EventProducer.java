package ru.yandex.practicum.telemetry.collector.service;

import jakarta.annotation.PreDestroy;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.kafka.serializer.GeneralAvroSerializer;

import java.util.Properties;

@Service
public class EventProducer {

    private final Producer<String, SpecificRecordBase> producer;

    public EventProducer() {
        Properties config = new Properties();
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, GeneralAvroSerializer.class);

        this.producer = new KafkaProducer<>(config);
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
