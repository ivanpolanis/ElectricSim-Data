package dev.str.electricsim.config.kafka;

import dev.str.electricsim.model.CammesaRecord;
import dev.str.electricsim.model.EnergySnapshot;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.kafka.support.serializer.JsonSerde;

import java.time.Duration;

@Configuration
@EnableKafkaStreams
public class StreamsConfig {

    @Bean
    public JsonSerde<CammesaRecord> cammesaRecordSerde() {
        return new JsonSerde<>(CammesaRecord.class);
    }

    @Bean
    public JsonSerde<EnergySnapshot> energySnapshotSerde() {
        return new JsonSerde<>(EnergySnapshot.class);
    }

    @Bean
    public KStream<String, EnergySnapshot> energySnapshotStream(
            StreamsBuilder builder,
            JsonSerde<CammesaRecord> cammesaRecordSerde,
            JsonSerde<EnergySnapshot> energySnapshotSerde
    ) {
        Duration window = Duration.ofMinutes(4);

        KStream<String, CammesaRecord> consumption = builder.stream("cammesa-consumption-raw", Consumed.with(Serdes.String(), cammesaRecordSerde));

        KStream<String, CammesaRecord> generation = builder.stream("cammesa-generation-raw", Consumed.with(Serdes.String(), cammesaRecordSerde));

        KStream<String, EnergySnapshot> partial = consumption.join(
                generation,
                (c, g) -> new EnergySnapshot(
                        c.timestamp().toString(),
                        c.value(),
                        g.value(),
                        0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,c.timestamp(),c.timestamp(),null,null),
                JoinWindows.ofTimeDifferenceWithNoGrace(window),
                StreamJoined.with(Serdes.String(), cammesaRecordSerde, cammesaRecordSerde)
        );

        partial.to("energy-snapshots",  Produced.with(Serdes.String(), energySnapshotSerde));
        return partial;

    }
}
