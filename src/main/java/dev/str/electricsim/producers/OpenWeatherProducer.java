package dev.str.electricsim.producers;

import dev.str.electricsim.dto.OpenWeatherRaw;
import dev.str.electricsim.model.WeatherRecord;
import org.apache.kafka.clients.admin.NewTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class OpenWeatherProducer {

    private final Logger log = LoggerFactory.getLogger(OpenWeatherProducer.class);
    private final KafkaTemplate<String, WeatherRecord> template;
    private final NewTopic openWeatherTopic;

    public OpenWeatherProducer(KafkaTemplate<String, WeatherRecord> template, NewTopic openWeatherTopic) {
        this.template = template;
        this.openWeatherTopic = openWeatherTopic;
    }

    public void send(OpenWeatherRaw raw) {
        if (raw == null) {
            log.warn("Attempted to send null OpenWeatherRaw record, skipping...");
            return;
        }

        if (raw.dt == null) {
            raw.dt = Instant.now().getEpochSecond();
        }

        WeatherRecord record = parse(raw);

        log.info("Sending message to OpenWeather topic: {}", record);
        template.send(openWeatherTopic.name(), "CABA", record);
    }

    private WeatherRecord parse(OpenWeatherRaw raw) {
        Double rain = null;
        if (raw.rain != null) {
            if (raw.rain.oneH != null) rain = raw.rain.oneH;
            else if (raw.rain.threeH != null) rain = raw.rain.threeH / 3.0; // promedio por hora
        }

        Double snow = null;
        if (raw.snow != null) {
            if (raw.snow.oneH != null) snow = raw.snow.oneH;
            else if (raw.snow.threeH != null) snow = raw.snow.threeH / 3.0;
        }

        return new WeatherRecord(
                raw.main != null ? raw.main.temp : null,
                raw.main != null ? raw.main.humidity : null,
                raw.main != null ? raw.main.pressure : null,
                raw.wind != null ? raw.wind.speed : null,
                raw.wind != null ? raw.wind.deg : null,
                raw.clouds != null ? raw.clouds.all : null,
                rain,
                snow,
                raw.sys != null ? raw.sys.sunrise : null,
                raw.sys != null ? raw.sys.sunset : null
        );
    }
}
