package dev.str.electricsim.services;

import dev.str.electricsim.cache.ConsumptionCacheManager;
import dev.str.electricsim.entity.ConsumptionEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class HistoricalConsumptionService {
    private final ConsumptionCacheManager consumptionCacheManager;

    public HistoricalConsumptionService(ConsumptionCacheManager consumptionCacheManager) {
        this.consumptionCacheManager = consumptionCacheManager;
    }

    public List<ConsumptionEntity> getHistoricalConsumptionForDate(LocalDate date) {
        return consumptionCacheManager.getOrFetch(date);
    }
}
