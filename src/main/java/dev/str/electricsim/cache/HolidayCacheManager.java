package dev.str.electricsim.cache;

import dev.str.electricsim.entity.HolidayEntity;
import dev.str.electricsim.repository.HolidayRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.time.LocalDate;

@Service
public class HolidayCacheManager extends DbCacheManager<HolidayEntity, HolidayEntity, HolidayRepository> {
    private static final Logger log = LoggerFactory.getLogger(HolidayCacheManager.class);

    private final RestClient holidayConnector;

    public HolidayCacheManager(HolidayRepository repository, RestClient consumptionConnector) {
        super(repository);
        this.holidayConnector = consumptionConnector;
    }

    @Override
    public HolidayEntity fetchFromApi(LocalDate date) {
        log.info("Fetching holiday data from API for date: {}", date);
        Boolean isHoliday = holidayConnector.get()
                .uri(uriBuilder -> uriBuilder.path("/EsDiaFeriado")
                        .queryParam("fecha", date.toString()).build())
                .retrieve()
                .body(Boolean.class);
        Boolean isWorkingDay = holidayConnector.get()
                .uri(uriBuilder -> uriBuilder.path("/EsDiaHabil")
                        .queryParam("fecha", date.toString()).build())
                .retrieve()
                .body(Boolean.class);
        return new HolidayEntity(date, isWorkingDay, isHoliday);
    }

    @Override
    public HolidayEntity getOrFetch(LocalDate date) {
        if (repository.existsByDay(date)) {
            return repository.findByDay(date);
        }

        log.info("Cache miss for holiday data on date {}. Fetching from API.", date);
        HolidayEntity data = fetchFromApi(date);
        repository.save(data);
        return data;
    }

}
