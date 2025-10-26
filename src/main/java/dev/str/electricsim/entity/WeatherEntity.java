package dev.str.electricsim.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneId;

@Entity
@Table(name = "weather")
public class WeatherEntity {
    @Id
    OffsetDateTime timestamp;

    LocalDate day;


    public WeatherEntity(OffsetDateTime timestamp) {
        this.timestamp = timestamp;
        this.day = timestamp.atZoneSameInstant(ZoneId.of("America/Argentina")).toLocalDate();
    }
}
