package dev.str.electricsim.producers;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.str.electricsim.dto.OpenWeatherRaw;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Component
public class OpenWeatherProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final ObjectMapper mapper = new ObjectMapper();

    @Value("${openweather.city}")
    private String city;

    @Value("${openweather.url}")
    private String url;

    public OpenWeatherProducer(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    // recibe un DTO ya mapeado por el scheduler; publicamos al topic
    public void publish(OpenWeatherRaw raw) {
        Map<String, Object> payload = new HashMap<>();
        long ts = raw.dt != null ? raw.dt : Instant.now().getEpochSecond();

        payload.put("timestamp", Instant.ofEpochSecond(ts).toString());
        payload.put("region", "CABA"); // o "CABA" fijo; o extraer de city
        payload.put("temperature", raw.main != null ? raw.main.temp : null);
        payload.put("humidity", raw.main != null ? raw.main.humidity : null);
        payload.put("pressure", raw.main != null ? raw.main.pressure : null);
        payload.put("wind_speed", raw.wind != null ? raw.wind.speed : null);
        payload.put("wind_direction", raw.wind != null ? raw.wind.deg : null);
        payload.put("clouds", raw.clouds != null ? raw.clouds.all : null);
        payload.put("sunrise", raw.sys != null && raw.sys.sunrise != null ? Instant.ofEpochSecond(raw.sys.sunrise).toString() : null);
        payload.put("sunset", raw.sys != null && raw.sys.sunset != null ? Instant.ofEpochSecond(raw.sys.sunset).toString() : null);
        // rain and snow: prefer 1h then 3h
        payload.put("rain", raw.rain != null ? (raw.rain.oneH != null ? raw.rain.oneH : raw.rain.threeH) : 0.0);
        payload.put("snow", raw.snow != null ? (raw.snow.oneH != null ? raw.snow.oneH : raw.snow.threeH) : 0.0);
        payload.put("source", "openweather");

        kafkaTemplate.send("weather", "CABA", payload);
    }
}
