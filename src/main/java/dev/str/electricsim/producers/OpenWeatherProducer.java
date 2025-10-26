package dev.str.electricsim.producers;

import dev.str.electricsim.dto.OpenWeatherRaw;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class OpenWeatherProducer {

    private final KafkaTemplate<String, OpenWeatherRaw> kafkaTemplate;
    private final NewTopic openWeatherTopic;

    @Value("${openweather.city}")
    private String city;

    @Value("${openweather.url}")
    private String url;

    public OpenWeatherProducer(KafkaTemplate<String, OpenWeatherRaw> kafkaTemplate, NewTopic openWeatherTopic) {
        this.kafkaTemplate = kafkaTemplate;
        this.openWeatherTopic = openWeatherTopic;
    }

    /**
     * Publica un mensaje crudo (DTO ya mapeado desde la API OpenWeather)
     * directamente al topic de Kafka, con clave = región.
     */
    public void publish(OpenWeatherRaw raw) {
        if (raw.dt == null) {
            raw.dt = Instant.now().getEpochSecond();
        }

        kafkaTemplate.send(openWeatherTopic.name(), "CABA", raw);
    }
}
