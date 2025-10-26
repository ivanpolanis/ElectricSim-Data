package dev.str.electricsim.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import jakarta.persistence.Id;

import java.time.OffsetDateTime;

public record EnergySnapshotRecord(
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
