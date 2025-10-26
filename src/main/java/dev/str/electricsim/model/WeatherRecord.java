package dev.str.electricsim.model;
public record WeatherRecord(
        Double temperature,
        Integer humidity,
        Double pressure,
        Double windSpeed,
        Integer windDirection,
        Integer clouds,
        Double rain,
        Double snow,
        Long sunrise,
        Long sunset ) {}