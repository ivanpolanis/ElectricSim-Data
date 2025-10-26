package dev.str.electricsim.producers;

import dev.str.electricsim.dto.OpenWeatherRaw;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class OpenWeatherProducer {

    private final KafkaTemplate<String, OpenWeatherRaw> kafkaTemplate;

    @Value("${openweather.city}")
    private String city;

    @Value("${openweather.url}")
    private String url;

    public OpenWeatherProducer(KafkaTemplate<String, OpenWeatherRaw> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    /**
     * Publica un mensaje crudo (DTO ya mapeado desde la API OpenWeather)
     * directamente al topic de Kafka, con clave = región.
     */
    public void publish(OpenWeatherRaw raw) {
        if (raw.dt == null) {
            raw.dt = Instant.now().getEpochSecond();
        }

        kafkaTemplate.send("openweather-raw", "CABA", raw);
    }
}
