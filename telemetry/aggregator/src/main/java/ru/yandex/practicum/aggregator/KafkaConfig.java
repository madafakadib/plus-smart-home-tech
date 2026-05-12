package ru.yandex.practicum.aggregator;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.yandex.practicum.kafka.deserializer.SensorEventDeserializer;
import ru.yandex.practicum.kafka.serializer.GeneralAvroSerializer;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

import java.util.Properties;

@Configuration
public class KafkaConfig {

    @Bean
    @ConfigurationProperties(prefix = "aggregator.kafka.consumer.properties")
    public Properties consumerProperties() {
        return new Properties();
    }

    @Bean
    @ConfigurationProperties(prefix = "aggregator.kafka.producer.properties")
    public Properties producerProperties() {
        return new Properties();
    }

    @Bean
    public KafkaConsumer<String, SensorEventAvro> kafkaConsumer(Properties consumerProperties) {
        consumerProperties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, SensorEventDeserializer.class);
        return new KafkaConsumer<>(consumerProperties);
    }

    @Bean
    public KafkaProducer<String, SensorsSnapshotAvro> kafkaProducer(Properties producerProperties) {
        producerProperties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, GeneralAvroSerializer.class);
        return new KafkaProducer<>(producerProperties);
    }
}
