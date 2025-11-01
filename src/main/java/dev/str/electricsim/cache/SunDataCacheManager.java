package dev.str.electricsim.cache;

import dev.str.electricsim.client.SunriseSunsetClient;
import dev.str.electricsim.entity.SunDataEntity;
import dev.str.electricsim.repository.SunDataRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
public class SunDataCacheManager extends DbCacheManager<SunDataEntity, SunDataEntity, SunDataRepository> {
    private static final Logger log = LoggerFactory.getLogger(SunDataCacheManager.class);

    private final SunriseSunsetClient sunriseSunsetClient;

    public SunDataCacheManager(SunDataRepository repository, SunriseSunsetClient sunriseSunsetClient) {
        super(repository);
        this.sunriseSunsetClient = sunriseSunsetClient;
    }

    @Override
    public SunDataEntity fetchFromApi(LocalDate date) {
        log.info("Fetching sunrise/sunset from API for date {}", date);

        Map<String, Object> results = sunriseSunsetClient.getSunData(date);
        if (results.isEmpty()) {
            log.warn("No sunrise/sunset data for date {}", date);
            return null;
        }

        return new SunDataEntity(
                date,
                (String) results.get("sunrise"),
                (String) results.get("sunset")
        );
    }

    @Override
    public SunDataEntity getOrFetch(LocalDate date) {
        return repository.findByDate(date).orElseGet(() -> {
            log.info("Cache miss for sunrise/sunset on {}. Fetching from API.", date);
            var entity = fetchFromApi(date);
            if (entity != null) {
                repository.save(entity);
            }
            return entity;
        });
    }
}
