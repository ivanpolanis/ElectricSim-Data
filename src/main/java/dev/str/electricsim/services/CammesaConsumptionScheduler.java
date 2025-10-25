package dev.str.electricsim.services;

import dev.str.electricsim.dto.CammesaRawConsumption;
import dev.str.electricsim.model.CammesaRecord;
import dev.str.electricsim.model.CammesaRecordType;
import dev.str.electricsim.producers.CammesaConsumptionProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.time.Duration;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.Arrays;

@Service
public class CammesaConsumptionScheduler {
    private final Logger log = LoggerFactory.getLogger(CammesaConsumptionScheduler.class);
    private static final Duration WINDOW = Duration.ofMinutes(10);

    private final RestClient consumptionConnector;
    private final CammesaConsumptionProducer producer;

    public CammesaConsumptionScheduler(RestClient consumptionConnector, CammesaConsumptionProducer producer) {
        this.consumptionConnector = consumptionConnector;
        this.producer = producer;
    }

    @Scheduled(cron = "${producers.cammesa.consumption.cron}")
    public void fetchConsumptionData() {
        log.info("Fetching consumption data from Cammesa...");
        CammesaRawConsumption[] data = consumptionConnector
                .get()
                .uri(uriBuilder -> uriBuilder.path("/ObtieneDemandaYTemperaturaRegion").queryParam("id_region", "426").build())
                .retrieve().body(CammesaRawConsumption[].class);

        if (data == null || data.length == 0) {
            log.warn("No consumption data retrieved from Cammesa.");
            return;
        }

        OffsetDateTime now = OffsetDateTime.now();
        Arrays.stream(data).filter(d -> d.timestamp() != null &&
                        !d.timestamp().isBefore(now.minus(WINDOW)) &&
                        !d.timestamp().isAfter(now.plus(WINDOW)))
                .findFirst()
                .map(d -> new CammesaRecord(d.timestamp(), d.demand(), CammesaRecordType.GENERATION, "CABA"))
                .ifPresent(producer::send);
    }
}
