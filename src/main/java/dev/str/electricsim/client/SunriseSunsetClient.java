package dev.str.electricsim.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;
import java.util.Map;

@Component
public class SunriseSunsetClient {
    private static final Logger log = LoggerFactory.getLogger(SunriseSunsetClient.class);
    private final RestTemplate restTemplate = new RestTemplate();

    private static final double LAT = -34.9214;  // La Plata, por ejemplo
    private static final double LNG = -57.9544;

    public Map<String, Object> getSunData(LocalDate date) {
        String url = UriComponentsBuilder
                .fromHttpUrl("https://api.sunrise-sunset.org/json")
                .queryParam("lat", LAT)
                .queryParam("lng", LNG)
                .queryParam("date", date)
                .queryParam("formatted", 0)
                .toUriString();

        log.info("Fetching sunrise/sunset for {}", date);

        var response = restTemplate.getForObject(url, Map.class);
        if (response == null || !"OK".equals(response.get("status"))) {
            log.warn("Failed to fetch sunrise/sunset for {}", date);
            return Map.of();
        }

        return (Map<String, Object>) response.get("results");
    }
}
