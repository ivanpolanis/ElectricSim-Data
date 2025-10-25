package dev.str.electricsim.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.OffsetDateTime;

public record CammesaRawConsumption(
        @JsonProperty("demHoy")
        Double demand,
        @JsonProperty("fecha")
        OffsetDateTime timestamp
) {
}
