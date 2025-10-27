package dev.str.electricsim.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.OffsetDateTime;

public record CammesaHistoricalConsumption(
        @JsonProperty("dem")
        Double demand,
        @JsonProperty("fecha")
        OffsetDateTime timestamp
) {
}
