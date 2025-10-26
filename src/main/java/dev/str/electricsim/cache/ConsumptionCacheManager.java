package dev.str.electricsim.cache;

import dev.str.electricsim.dto.CammesaHistoricalConsumption;
import dev.str.electricsim.dto.CammesaRawConsumption;
import dev.str.electricsim.entity.ConsumptionEntity;
import dev.str.electricsim.repository.ConsumptionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Service
public class ConsumptionCacheManager extends DbCacheManager<ConsumptionEntity, ConsumptionRepository> {

    private final static Logger log = LoggerFactory.getLogger(ConsumptionCacheManager.class);

    private final RestClient consumptionConnector;

    public ConsumptionCacheManager(ConsumptionRepository repository, RestClient consumptionConnector) {
        super(repository);
        this.consumptionConnector = consumptionConnector;
    }


    @Override
    public List<ConsumptionEntity> fetchFromApi(LocalDate date) {
        CammesaHistoricalConsumption[] data = consumptionConnector.get().uri(uriBuilder -> uriBuilder.path("/ObtieneDemandaYTemperaturaRegionByFecha").queryParam("fecha", date.toString()).queryParam("id_region", "426").build())
                .retrieve()
                .body(CammesaHistoricalConsumption[].class);

        if (data == null || data.length == 0) {
            log.warn("No consumption data retrieved from Cammesa for date: {}", date);
            return null;
        }
        return Arrays.stream(data).map(this::mapToEntities).filter(e -> e.getDay().isEqual(date)).toList();
    }

    @Override
    public List<ConsumptionEntity> getOrFetch(LocalDate date) {
        if (repository.existsByDay(date)) {
            return repository.findAllByDay(date);
        } ;
        List<ConsumptionEntity> data = fetchFromApi(date);
        repository.saveAll(data);
        return data;
    }

    private ConsumptionEntity mapToEntities(CammesaHistoricalConsumption data) {
        return new ConsumptionEntity(
                data.timestamp(),
                data.demand()
        );
    }
}
