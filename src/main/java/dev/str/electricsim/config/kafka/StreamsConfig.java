package dev.str.electricsim.config.kafka;

import dev.str.electricsim.model.CammesaRecord;
import dev.str.electricsim.model.EnergySnapshotRecord;
import dev.str.electricsim.model.WeatherRecord;
import org.apache.kafka.streams.kstream.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.kafka.support.serializer.JsonSerde;

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
    public JsonSerde<EnergySnapshotRecord> energySnapshotSerde() {
        JsonSerde<EnergySnapshotRecord> serde = new JsonSerde<>(EnergySnapshotRecord.class);
        serde.ignoreTypeHeaders();
        return serde;
    }

    @Bean
    public JsonSerde<WeatherRecord> weatherRecordSerde() {
        JsonSerde<WeatherRecord> serde = new JsonSerde<>(WeatherRecord.class);
        serde.ignoreTypeHeaders();
        return serde;
    }


}
