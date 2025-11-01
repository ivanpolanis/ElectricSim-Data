package dev.str.electricsim.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "sun_data")
public class SunDataEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private LocalDate date;

    @Column(nullable = false)
    private String sunrise; // ISO8601 UTC o local según prefieras

    @Column(nullable = false)
    private String sunset;

    public SunDataEntity() {}

    public SunDataEntity(LocalDate date, String sunrise, String sunset) {
        this.date = date;
        this.sunrise = sunrise;
        this.sunset = sunset;
    }

    public Long getId() { return id; }
    public LocalDate getDate() { return date; }
    public String getSunrise() { return sunrise; }
    public String getSunset() { return sunset; }

    public void setDate(LocalDate date) { this.date = date; }
    public void setSunrise(String sunrise) { this.sunrise = sunrise; }
    public void setSunset(String sunset) { this.sunset = sunset; }
}
