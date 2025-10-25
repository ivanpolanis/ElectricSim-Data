package dev.str.electricsim.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.OffsetDateTime;

public record CammesaRawGeneration(
        @JsonProperty("sumTotal")
        Double energyGenerated,
        @JsonProperty("fecha")
        OffsetDateTime timestamp
) {
}
