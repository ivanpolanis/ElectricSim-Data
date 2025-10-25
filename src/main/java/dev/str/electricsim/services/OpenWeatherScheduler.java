package dev.str.electricsim.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.str.electricsim.dto.OpenWeatherRaw;
import dev.str.electricsim.producers.OpenWeatherProducer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class OpenWeatherScheduler {

    private final RestTemplate rest = new RestTemplate();
    private final ObjectMapper mapper = new ObjectMapper();
    private final OpenWeatherProducer producer;

    @Value("${openweather.api.key:}")
    private String apiKey;

    @Value("${openweather.url}")
    private String url;

    @Value("${openweather.city}")
    private String city;

    public OpenWeatherScheduler(OpenWeatherProducer producer) {
        this.producer = producer;
    }

    @Scheduled(cron = "0 */5 * * * *") // cada 5 minutos en los minutos 0,5,10...
    public void fetchAndPublish() {
        if (apiKey == null || apiKey.isBlank()) {
            // log y salir
            System.err.println("OpenWeather API key not set. Set OPENWEATHER_API_KEY env var or property openweather.api.key");
            return;
        }

        try {
            String requestUrl = String.format("%s?q=%s&appid=%s&units=%s", url, java.net.URLEncoder.encode(city, "UTF-8"), apiKey, "metric");
            ResponseEntity<String> resp = rest.getForEntity(requestUrl, String.class);
            if (resp.getStatusCode().is2xxSuccessful() && resp.getBody() != null) {
                OpenWeatherRaw raw = mapper.readValue(resp.getBody(), OpenWeatherRaw.class);
                producer.publish(raw);
            } else {
                System.err.println("OpenWeather non-200: " + resp.getStatusCodeValue());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
