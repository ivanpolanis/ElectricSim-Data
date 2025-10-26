package dev.str.electricsim.config.kafka;

import dev.str.electricsim.model.CammesaRecord;
import dev.str.electricsim.model.EnergySnapshot;
import dev.str.electricsim.model.WeatherRecord;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.kafka.support.serializer.JsonSerde;

import java.time.Duration;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.sql.Timestamp;

@Configuration
@EnableKafkaStreams
public class StreamsConfig {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Bean
    public JsonSerde<CammesaRecord> cammesaRecordSerde() {
        JsonSerde<CammesaRecord> serde = new JsonSerde<>(CammesaRecord.class);
        serde.ignoreTypeHeaders();
        return serde;
    }

    @Bean
    public JsonSerde<EnergySnapshot> energySnapshotSerde() {
        JsonSerde<EnergySnapshot> serde = new JsonSerde<>(EnergySnapshot.class);
        serde.ignoreTypeHeaders();
        return serde;
    }

    @Bean
    public JsonSerde<WeatherRecord> weatherRecordSerde() {
        JsonSerde<WeatherRecord> serde = new JsonSerde<>(WeatherRecord.class);
        serde.ignoreTypeHeaders();
        return serde;
    }

    @Bean
    public KStream<String, EnergySnapshot> energySnapshotStream(
            StreamsBuilder builder,
            JsonSerde<CammesaRecord> cammesaRecordSerde,
            JsonSerde<EnergySnapshot> energySnapshotSerde,
            JsonSerde<WeatherRecord> weatherRecordSerde
    ) {

        Duration window = Duration.ofMinutes(4);

        KStream<String, CammesaRecord> consumption =
                builder.stream("cammesa-consumption-raw", Consumed.with(Serdes.String(), cammesaRecordSerde));

        KStream<String, CammesaRecord> generation =
                builder.stream("cammesa-generation-raw", Consumed.with(Serdes.String(), cammesaRecordSerde));

        KStream<String, WeatherRecord> weather =
                builder.stream("openweather-raw", Consumed.with(Serdes.String(), weatherRecordSerde));

        KStream<String, EnergySnapshot> partial = consumption.join(
                generation,
                (c, g) -> new EnergySnapshot(
                        c.timestamp().toString(),
                        c.value(),
                        g.value(),
                        0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
                        c.timestamp(), c.timestamp(),
                        null, null
                ),
                JoinWindows.ofTimeDifferenceWithNoGrace(window),
                StreamJoined.with(Serdes.String(), cammesaRecordSerde, cammesaRecordSerde)
        );

        KStream<String, EnergySnapshot> enriched = partial.join(
                weather,
                (snapshot, w) -> new EnergySnapshot(
                        snapshot.date(),
                        snapshot.consumption(),
                        snapshot.generation(),
                        w.temperature(),
                        w.humidity() != null ? w.humidity().doubleValue() : 0.0,
                        w.rain(),
                        w.snow(),
                        w.pressure(),
                        w.windSpeed(),
                        w.windDirection() != null ? w.windDirection().doubleValue() : 0.0,
                        w.clouds() != null ? w.clouds().doubleValue() : 0.0,
                        OffsetDateTime.ofInstant(Instant.ofEpochSecond(w.sunrise()), ZoneOffset.UTC),
                        OffsetDateTime.ofInstant(Instant.ofEpochSecond(w.sunset()), ZoneOffset.UTC),
                        snapshot.working_day(),
                        snapshot.holiday()
                ),
                JoinWindows.ofTimeDifferenceWithNoGrace(window),
                StreamJoined.with(Serdes.String(), energySnapshotSerde, weatherRecordSerde)
        );

        enriched.to("energy-snapshots", Produced.with(Serdes.String(), energySnapshotSerde));

        final String insertSql = """
            INSERT INTO energy_snapshots (
                        "key",
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
            ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;

        enriched.foreach((key, snapshot) -> {
            try {
                jdbcTemplate.update(
                        insertSql,
                        key,
                        snapshot.date() != null
                                ? Timestamp.from(OffsetDateTime.parse(snapshot.date()).toInstant())
                                : null,
                        snapshot.consumption(),
                        snapshot.generation(),
                        snapshot.temperature(),
                        snapshot.humidity(),
                        snapshot.rain(),
                        snapshot.snow(),
                        snapshot.pressure(),
                        snapshot.wind_speed(),
                        snapshot.wind_direction(),
                        snapshot.clouds(),
                        snapshot.sunrise() != null ? Timestamp.from(snapshot.sunrise().toInstant()) : null,
                        snapshot.sunset() != null ? Timestamp.from(snapshot.sunset().toInstant()) : null,
                        snapshot.working_day(),
                        snapshot.holiday()
                );
            } catch (Exception e) {
                System.err.println("Error insertando en Postgres: " + e.getMessage());
                e.printStackTrace();
            }
        });

        return enriched;
    }
}
