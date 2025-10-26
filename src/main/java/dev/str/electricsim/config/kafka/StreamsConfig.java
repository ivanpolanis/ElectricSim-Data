package dev.str.electricsim.config.kafka;

import dev.str.electricsim.model.CammesaRecord;
import dev.str.electricsim.model.EnergySnapshot;
import dev.str.electricsim.model.WeatherRecord;
import dev.str.electricsim.dto.OpenWeatherRaw;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.kafka.support.serializer.JsonSerde;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.Instant;

@Configuration
@EnableKafkaStreams
public class StreamsConfig {

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
    public JsonSerde<OpenWeatherRaw> openWeatherRawSerde() {
        JsonSerde<OpenWeatherRaw> serde = new JsonSerde<>(OpenWeatherRaw.class);
        serde.ignoreTypeHeaders();
        return serde;
    }

    @Bean
    public KStream<String, EnergySnapshot> energySnapshotStream(
            StreamsBuilder builder,
            JsonSerde<CammesaRecord> cammesaRecordSerde,
            JsonSerde<EnergySnapshot> energySnapshotSerde,
            JsonSerde<WeatherRecord> weatherRecordSerde,
            JsonSerde<OpenWeatherRaw> openWeatherRawSerde
    ) {

        Duration window = Duration.ofMinutes(4);

        // 🔹 Streams base
        KStream<String, CammesaRecord> consumption =
                builder.stream("cammesa-consumption-raw", Consumed.with(Serdes.String(), cammesaRecordSerde));

        KStream<String, CammesaRecord> generation =
                builder.stream("cammesa-generation-raw", Consumed.with(Serdes.String(), cammesaRecordSerde));

        // 🔹 Stream de OpenWeatherRaw → transformado a WeatherRecord
        KStream<String, OpenWeatherRaw> weatherRaw =
                builder.stream("openweather-raw", Consumed.with(Serdes.String(), openWeatherRawSerde));

        KStream<String, WeatherRecord> weather = weatherRaw.mapValues(raw -> new WeatherRecord(
                raw.main != null ? raw.main.temp : 0.0,
                raw.main != null ? raw.main.humidity : 0,
                raw.main != null ? raw.main.pressure : 0.0,
                raw.wind != null ? raw.wind.speed : 0.0,
                raw.wind != null ? raw.wind.deg : 0,
                raw.clouds != null ? raw.clouds.all : 0,
                raw.rain != null ? (raw.rain.oneH != null ? raw.rain.oneH : 0.0) : 0.0,
                raw.snow != null ? (raw.snow.oneH != null ? raw.snow.oneH : 0.0) : 0.0,
                raw.sys != null ? raw.sys.sunrise : 0L,
                raw.sys != null ? raw.sys.sunset : 0L
        ));

        // 🔹 JOIN 1: consumo + generación
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

        // 🔹 JOIN 2: snapshot parcial + weather → completo
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

        // 🔹 Emitimos resultado final
        enriched.to("energy-snapshots", Produced.with(Serdes.String(), energySnapshotSerde));

        return enriched;
    }
}
