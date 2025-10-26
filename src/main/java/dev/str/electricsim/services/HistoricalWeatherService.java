package dev.str.electricsim.services;

import dev.str.electricsim.cache.WeatherCacheManager;
import dev.str.electricsim.entity.WeatherEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class HistoricalWeatherService {
    private final WeatherCacheManager weatherCacheManager;

    public HistoricalWeatherService(WeatherCacheManager weatherCacheManager) {
        this.weatherCacheManager = weatherCacheManager;
    }

    public List<WeatherEntity> getHistoricalWeatherForDate(LocalDate date) {
        return weatherCacheManager.getOrFetch(date);
    }

}
