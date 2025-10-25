package dev.str.electricsim.services;

import dev.str.electricsim.dto.CammesaRawGeneration;
import dev.str.electricsim.model.CammesaRecord;
import dev.str.electricsim.model.CammesaRecordType;
import dev.str.electricsim.producers.CammesaGenerationProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.Arrays;

@Service
public class CammesaGenerationScheduler {
    private final Logger log = LoggerFactory.getLogger(CammesaGenerationScheduler.class);
    private static final Duration WINDOW = Duration.ofMinutes(15);

    private final RestClient generationConnector;
    private final CammesaGenerationProducer producer;

    public CammesaGenerationScheduler(RestClient generationConnector, CammesaGenerationProducer producer) {
        this.generationConnector = generationConnector;
        this.producer = producer;
    }

    @Scheduled(cron = "${producers.cammesa.generation.cron}")
    public void fetchConsumptionData() {
        log.info("Fetching generating data from Cammesa...");
        CammesaRawGeneration[] data = generationConnector
                .get()
                .uri(uriBuilder -> uriBuilder.path("/ObtieneGeneracioEnergiaPorRegion").queryParam("id_region", "426").build())
                .retrieve().body(CammesaRawGeneration[].class);

        if (data == null || data.length == 0) {
            log.warn("No consumption data retrieved from Cammesa.");
            return;
        }

        OffsetDateTime now = OffsetDateTime.now();
        Arrays.stream(data).filter(d -> d.timestamp() != null &&
                        !d.timestamp().isBefore(now.minus(WINDOW)) &&
                        !d.timestamp().isAfter(now.plus(WINDOW)))
                .findFirst()
                .map(d -> new CammesaRecord(d.timestamp(), d.energyGenerated(), CammesaRecordType.GENERATION, "CABA"))
                .ifPresent(producer::send);
    }
}
