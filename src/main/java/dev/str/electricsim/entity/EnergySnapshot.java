package dev.str.electricsim.entity;

import jakarta.persistence.*;

import java.time.OffsetDateTime;

@Entity
@Table(name = "energy_snapshots")
public class EnergySnapshot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String date;
    private Double consumption;
    private Double generation;
    private Double temperature;
    private Double humidity;
    private Double rain;
    private Double snow;
    private Double pressure;
    private Double wind_speed;
    private Double wind_direction;
    private Double clouds;
    private OffsetDateTime sunrise;
    private OffsetDateTime sunset;
    private Boolean working_day;
    private Boolean holiday;

    public EnergySnapshot(String date, Double consumption, Double generation, Double temperature, Double humidity, Double rain, Double snow, Double pressure, Double wind_speed, Double wind_direction, Double clouds, OffsetDateTime sunrise, OffsetDateTime sunset, Boolean working_day, Boolean holiday) {
        this.date = date;
        this.consumption = consumption;
        this.generation = generation;
        this.temperature = temperature;
        this.humidity = humidity;
        this.rain = rain;
        this.snow = snow;
        this.pressure = pressure;
        this.wind_speed = wind_speed;
        this.wind_direction = wind_direction;
        this.clouds = clouds;
        this.sunrise = sunrise;
        this.sunset = sunset;
        this.working_day = working_day;
        this.holiday = holiday;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Double getConsumption() {
        return consumption;
    }

    public void setConsumption(Double consumption) {
        this.consumption = consumption;
    }

    public Double getGeneration() {
        return generation;
    }

    public void setGeneration(Double generation) {
        this.generation = generation;
    }

    public Double getTemperature() {
        return temperature;
    }

    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }

    public Double getHumidity() {
        return humidity;
    }

    public void setHumidity(Double humidity) {
        this.humidity = humidity;
    }

    public Double getRain() {
        return rain;
    }

    public void setRain(Double rain) {
        this.rain = rain;
    }

    public Double getSnow() {
        return snow;
    }

    public void setSnow(Double snow) {
        this.snow = snow;
    }

    public Double getPressure() {
        return pressure;
    }

    public void setPressure(Double pressure) {
        this.pressure = pressure;
    }

    public Double getWind_speed() {
        return wind_speed;
    }

    public void setWind_speed(Double wind_speed) {
        this.wind_speed = wind_speed;
    }

    public Double getWind_direction() {
        return wind_direction;
    }

    public void setWind_direction(Double wind_direction) {
        this.wind_direction = wind_direction;
    }

    public Double getClouds() {
        return clouds;
    }

    public void setClouds(Double clouds) {
        this.clouds = clouds;
    }

    public OffsetDateTime getSunrise() {
        return sunrise;
    }

    public void setSunrise(OffsetDateTime sunrise) {
        this.sunrise = sunrise;
    }

    public OffsetDateTime getSunset() {
        return sunset;
    }

    public void setSunset(OffsetDateTime sunset) {
        this.sunset = sunset;
    }

    public Boolean getWorking_day() {
        return working_day;
    }

    public void setWorking_day(Boolean working_day) {
        this.working_day = working_day;
    }

    public Boolean getHoliday() {
        return holiday;
    }

    public void setHoliday(Boolean holiday) {
        this.holiday = holiday;
    }
}
