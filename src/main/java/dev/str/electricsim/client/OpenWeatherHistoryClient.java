package dev.str.electricsim.client;

import dev.str.electricsim.client.model.WeatherResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

@Component
public class OpenWeatherHistoryClient {
    @Value("${openweather.api.key}")
    private String apiKey;

    @Value("${openweather.city.lat}")
    private String lat;

    @Value("${openweather.city.lon}")
    private String lon;

    @Value("${openweather.units}")
    private String units;

    private final RestClient weatherHistoryConnector;
    public OpenWeatherHistoryClient(RestClient weatherHistoryConnector) {
        this.weatherHistoryConnector = weatherHistoryConnector;
    }

    public List<WeatherResponse.WeatherItem> getWeatherHistory(LocalDate date) {
        long startEpoch = date.atStartOfDay().atZone(ZoneId.systemDefault()).toEpochSecond();

        WeatherResponse data = weatherHistoryConnector.get().uri(uriBuilder -> uriBuilder
                .path("/city")
                .queryParam("lat", lat)
                .queryParam("lon", lon)
                .queryParam("appid", apiKey)
                .queryParam("units", units)
                .queryParam("type", "hour")
                .queryParam("start", startEpoch)
                .build())
                .retrieve()
                .body(WeatherResponse.class);

        if (data == null) {
            throw new RuntimeException("No weather data retrieved from OpenWeather");
        }

        return data.list();

    }
}
