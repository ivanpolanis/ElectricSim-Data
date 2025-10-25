package dev.str.electricsim.model;

import java.time.OffsetDateTime;

public record CammesaRecord(
        OffsetDateTime timestamp,
        Double value,
        CammesaRecordType type,
        String region
) {
}
