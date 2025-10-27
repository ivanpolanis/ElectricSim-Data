package dev.str.electricsim.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneId;

@Entity
@Table(name = "cammesa_consumptions")
public class ConsumptionEntity {
    @Id
    OffsetDateTime timestamp;

    Double consumption;
    LocalDate day;

    public ConsumptionEntity(OffsetDateTime timestamp, Double consumption) {
        this.timestamp = timestamp;
        this.consumption = consumption;
        this.day = timestamp.atZoneSameInstant(ZoneId.systemDefault()).toLocalDate();
    }

    public ConsumptionEntity() {

    }

    public OffsetDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(OffsetDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public Double getConsumption() {
        return consumption;
    }

    public void setConsumption(Double consumption) {
        this.consumption = consumption;
    }

    public LocalDate getDay() {
        return day;
    }

    public void setDay(LocalDate day) {
        this.day = day;
    }
}
