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
}
