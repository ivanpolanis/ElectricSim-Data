package dev.str.electricsim.client.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.OffsetDateTime;
import java.util.List;

public record WeatherResponse(
        String message,
        String cod,
        int city_id,
        double calctime,
        int cnt,
        List<WeatherItem> list
) {

    public record WeatherItem(
            OffsetDateTime dt,
            Main main,
            Wind wind,
            Clouds clouds,
            List<WeatherDescription> weather,
            Rain rain,
            Snow snow
    ) {
    }

    public record Main(
            double temp,
            double feels_like,
            double pressure,
            int humidity,
            double temp_min,
            double temp_max
    ) {
    }

    public record Wind(
            double speed,
            int deg
    ) {
    }

    public record Clouds(
            int all
    ) {
    }

    public record WeatherDescription(
            int id,
            String main,
            String description,
            String icon
    ) {
    }

    public record Rain(
            @JsonProperty("1h")
            Double oneH,
            @JsonProperty("3h")
            Double threeH
    ) {
    }

    public record Snow(
            @JsonProperty("1h")
            Double oneH,
            @JsonProperty("3h")
            Double threeH
    ) {
    }
}