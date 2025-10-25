package dev.str.electricsim.model;

import java.time.OffsetDateTime;

public record EnergySnapshot(
        String date,
        Double consumption,
        Double generation,
        Double temperature,
        Double humidity,
        Double rain,
        Double snow,
        Double pressure,
        Double wind_speed,
        Double wind_direction,
        Double clouds,
        OffsetDateTime sunrise,
        OffsetDateTime sunset,
        Boolean working_day,
        Boolean holiday
) {
}
