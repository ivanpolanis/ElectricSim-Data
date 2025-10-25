package dev.str.electricsim.services;

import dev.str.electricsim.dto.CammesaRawConsumption;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class CammesaConsumptionScheduler {
    private final Logger log = LoggerFactory.getLogger(CammesaConsumptionScheduler.class);
    private final RestClient consumptionConnector;
    public CammesaConsumptionScheduler(RestClient consumptionConnector) {
        this.consumptionConnector = consumptionConnector;
    }

    @Scheduled(cron = "${producers.cammesa.consumption.cron}")
    public void fetchConsumptionData() {
        log.info("Fetching consumption data from Cammesa...");
        CammesaRawConsumption[] data = consumptionConnector
                .get()
                .uri(uriBuilder -> uriBuilder.path("/ObtieneDemandaYTemperaturaRegion").queryParam("id_region","426").build())
                .retrieve().body(CammesaRawConsumption[].class);
        log.info("Cammesa consumption data retrieved successfully. Records fetched: {}", data.length);

    }
}
