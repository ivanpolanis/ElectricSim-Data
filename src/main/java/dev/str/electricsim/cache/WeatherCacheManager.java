package dev.str.electricsim.cache;

import dev.str.electricsim.client.OpenWeatherHistoryClient;
import dev.str.electricsim.client.model.WeatherResponse;
import dev.str.electricsim.entity.WeatherEntity;
import dev.str.electricsim.repository.WeatherRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class WeatherCacheManager extends DbCacheManager<WeatherEntity, List<WeatherEntity>, WeatherRepository> {
    private static final Logger log = LoggerFactory.getLogger(WeatherCacheManager.class);

    private final OpenWeatherHistoryClient openWeatherHistoryClient;
    public WeatherCacheManager(WeatherRepository repository, OpenWeatherHistoryClient openWeatherHistoryClient) {
        super(repository);
        this.openWeatherHistoryClient = openWeatherHistoryClient;
    }

    @Override
    public List<WeatherEntity> fetchFromApi(LocalDate date) {
        log.info("Fetching weather for date: {}", date);
        var response = openWeatherHistoryClient.getWeatherHistory(date);
        if (response == null || response.toArray().length == 0) {
            return List.of();
        }
        return response.stream().map(this::mapToWeatherEntity).toList();
    }

    @Override
    public List<WeatherEntity> getOrFetch(LocalDate date) {
        if (repository.existsByDay(date)) {
            return repository.findAllByDay(date);
        }

        log.info("Cache miss for weather data on date {}. Fetching from API.", date);

        var data = fetchFromApi(date);
        repository.saveAll(data);
        return data;
    }

    private WeatherEntity mapToWeatherEntity(WeatherResponse.WeatherItem item) {
        return new WeatherEntity(
                item.dt(),
                item.main().temp(),
                item.main().humidity(),
                item.main().pressure(),
                item.wind().speed(),
                item.wind().deg(),
                item.clouds().all(),
                item.rain() != null ? item.rain().oneH() : 0.0,
                item.snow() != null ? item.snow().oneH() : 0.0
        );
    }
}
