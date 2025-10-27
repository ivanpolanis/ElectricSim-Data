package dev.str.electricsim.services;

import dev.str.electricsim.cache.HolidayCacheManager;
import dev.str.electricsim.entity.HolidayEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class HolidayService {
    private final HolidayCacheManager holidayCacheManager;

    public HolidayService(HolidayCacheManager holidayCacheManager) {
        this.holidayCacheManager = holidayCacheManager;
    }

    public HolidayEntity getHolidayStatusForDate(LocalDate date) {
        return holidayCacheManager.getOrFetch(date);
    }
}
