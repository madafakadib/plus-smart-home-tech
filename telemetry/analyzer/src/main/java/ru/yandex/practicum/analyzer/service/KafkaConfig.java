package ru.yandex.practicum.analyzer.service;

import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

import java.util.Properties;

@Configuration
public class KafkaConfig {

    @Bean
    @ConfigurationProperties(prefix = "analyzer.kafka.hubs.properties")
    public Properties hubConsumerProperties() {
        return new Properties();
    }

    @Bean
    @ConfigurationProperties(prefix = "analyzer.kafka.snapshots.properties")
    public Properties snapshotConsumerProperties() {
        return new Properties();
    }

    @Bean
    public KafkaConsumer<String, HubEventAvro> hubEventConsumer(Properties hubConsumerProperties) {
        return new KafkaConsumer<>(hubConsumerProperties);
    }

    @Bean
    public KafkaConsumer<String, SensorsSnapshotAvro> snapshotConsumer(Properties snapshotConsumerProperties) {
        return new KafkaConsumer<>(snapshotConsumerProperties);
    }

}
