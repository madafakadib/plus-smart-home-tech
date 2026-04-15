import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"controller", "service", "mapper"})
public class TelemetryCollectorApp {
    public static void main(String[] args) {
        SpringApplication.run(TelemetryCollectorApp.class, args);
    }
}
