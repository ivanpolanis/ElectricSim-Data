package dev.str.electricsim.model;

import dev.str.electricsim.entity.EnergySnapshot;

import java.time.OffsetDateTime;

public record EnergySnapshotRecord(
        String date,
        Double consumption,
        Double generation,
        Double temperature,
        Integer humidity,
        Double rain,
        Double snow,
        Double pressure,
        Double wind_speed,
        Integer wind_direction,
        Integer clouds,
        OffsetDateTime sunrise,
        OffsetDateTime sunset,
        Boolean working_day,
        Boolean holiday
) {
    public EnergySnapshot toEnergySnapshot() {
        return new EnergySnapshot(
                date,
                consumption,
                generation,
                temperature,
                humidity,
                rain,
                snow,
                pressure,
                wind_speed,
                wind_direction,
                clouds,
                sunrise,
                sunset,
                working_day,
                holiday
        );
    }

    public String toCsvRecord() {
        return String.join(",",
                date,
                consumption != null ? consumption.toString() : "",
                temperature != null ? temperature.toString() : "",
                humidity != null ? humidity.toString() : "",
                rain != null ? rain.toString() : "",
                snow != null ? snow.toString() : "",
                pressure != null ? pressure.toString() : "",
                wind_speed != null ? wind_speed.toString() : "",
                wind_direction != null ? wind_direction.toString() : "",
                clouds != null ? clouds.toString() : "",
                sunrise != null ? String.valueOf(sunrise.toEpochSecond()) : "",
                sunset != null ? String.valueOf(sunset.toEpochSecond()) : "",
                working_day != null ? working_day.toString() : "",
                holiday != null ? holiday.toString() : ""
        );
    }
}
