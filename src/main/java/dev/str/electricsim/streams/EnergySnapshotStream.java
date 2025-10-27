package dev.str.electricsim.streams;

import dev.str.electricsim.cache.HolidayCacheManager;
import dev.str.electricsim.entity.HolidayEntity;
import dev.str.electricsim.model.CammesaRecord;
import dev.str.electricsim.model.EnergySnapshotRecord;
import dev.str.electricsim.model.WeatherRecord;
import dev.str.electricsim.repository.EnergySnapshotRepository;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.*;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.support.serializer.JsonSerde;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Component
public class EnergySnapshotStream {

    private final EnergySnapshotRepository energySnapshotRepository;
    private final HolidayCacheManager holidayCacheManager;

    public EnergySnapshotStream(EnergySnapshotRepository energySnapshotRepository,
                                HolidayCacheManager holidayCacheManager) {
        this.energySnapshotRepository = energySnapshotRepository;
        this.holidayCacheManager = holidayCacheManager;
    }

    @Bean
    public KStream<String, EnergySnapshotRecord> stream(
            StreamsBuilder builder,
            JsonSerde<CammesaRecord> cammesaRecordSerde,
            JsonSerde<EnergySnapshotRecord> energySnapshotSerde,
            JsonSerde<WeatherRecord> weatherRecordSerde
    ) {

        Duration window = Duration.ofMinutes(4);

        KStream<String, CammesaRecord> consumption =
                builder.stream("cammesa-consumption-raw", Consumed.with(Serdes.String(), cammesaRecordSerde));

        KStream<String, CammesaRecord> generation =
                builder.stream("cammesa-generation-raw", Consumed.with(Serdes.String(), cammesaRecordSerde));

        KStream<String, WeatherRecord> weather =
                builder.stream("openweather-raw", Consumed.with(Serdes.String(), weatherRecordSerde));

        KStream<String, EnergySnapshotRecord> partial = consumption.join(
                generation,
                (c, g) -> new EnergySnapshotRecord(
                        c.timestamp().toString(),
                        c.value(),
                        g.value(),
                        0.0, 0, 0.0, 0.0, 0.0, 0.0, 0, 0,
                        c.timestamp(), c.timestamp(),
                        null, null
                ),
                JoinWindows.ofTimeDifferenceWithNoGrace(window),
                StreamJoined.with(Serdes.String(), cammesaRecordSerde, cammesaRecordSerde)
        );

        KStream<String, EnergySnapshotRecord> enriched = partial.join(
                weather,
                (snapshot, w) -> {
                    LocalDate date = LocalDate.parse(snapshot.date().substring(0, 10));
                    HolidayEntity holidayInfo = holidayCacheManager.getOrFetch(date);

                    return new EnergySnapshotRecord(
                            snapshot.date(),
                            snapshot.consumption(),
                            snapshot.generation(),
                            w.temperature(),
                            w.humidity() != null ? w.humidity() : 0,
                            w.rain(),
                            w.snow(),
                            w.pressure(),
                            w.windSpeed(),
                            w.windDirection() != null ? w.windDirection() : 0,
                            w.clouds() != null ? w.clouds() : 0,
                            OffsetDateTime.ofInstant(Instant.ofEpochSecond(w.sunrise()), ZoneOffset.UTC),
                            OffsetDateTime.ofInstant(Instant.ofEpochSecond(w.sunset()), ZoneOffset.UTC),
                            holidayInfo.isWorkingDay(),
                            holidayInfo.isHoliday()
                    );
                },
                JoinWindows.ofTimeDifferenceWithNoGrace(window),
                StreamJoined.with(Serdes.String(), energySnapshotSerde, weatherRecordSerde)
        );

        enriched.to("energy-snapshots", Produced.with(Serdes.String(), energySnapshotSerde));

        enriched.foreach((key, snapshot) -> {
            energySnapshotRepository.save(snapshot.toEnergySnapshot());
        });

        return enriched;
    }
}
