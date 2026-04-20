package ru.yandex.practicum.telemetry.collector;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"ru/yandex/practicum/telemetry/collector", "ru/yandex/practicum/telemetry/collector/service", "ru/yandex/practicum/telemetry/collector/mapper"})
public class TelemetryCollectorApp {
    public static void main(String[] args) {
        SpringApplication.run(TelemetryCollectorApp.class, args);
    }
}
