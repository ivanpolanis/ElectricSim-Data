package dev.str.electricsim.services;

import dev.str.electricsim.cache.SunDataCacheManager;
import dev.str.electricsim.entity.ConsumptionEntity;
import dev.str.electricsim.entity.EnergySnapshot;
import dev.str.electricsim.entity.HolidayEntity;
import dev.str.electricsim.entity.WeatherEntity;
import dev.str.electricsim.model.EnergySnapshotRecord;
import org.springframework.stereotype.Service;
import dev.str.electricsim.entity.SunDataEntity;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.time.OffsetDateTime;


@Service
public class HistoricalService {
    private final HolidayService holidayService;
    private final HistoricalWeatherService historicalWeatherService;
    private final HistoricalConsumptionService historicalConsumptionService;
    private final SunDataCacheManager sunDataCacheManager;

    public HistoricalService(HolidayService holidayService,HistoricalWeatherService historicalWeatherService, HistoricalConsumptionService historicalConsumptionService, SunDataCacheManager sunDataCacheManager) {
        this.holidayService = holidayService;
        this.historicalWeatherService = historicalWeatherService;
        this.historicalConsumptionService = historicalConsumptionService;
        this.sunDataCacheManager = sunDataCacheManager;
    }

    public List<List<EnergySnapshotRecord>> getHistoricalWeatherBetweenDates(LocalDate start, LocalDate end) {
        List<List<EnergySnapshotRecord>> result = new java.util.ArrayList<>();
        for (LocalDate date = start; !date.isAfter(end); date = date.plusDays(1)) {
            var holiday = holidayService.getHolidayStatusForDate(date);
            var g = historicalWeatherService.getHistoricalWeatherForDate(date);
            var w = historicalConsumptionService.getHistoricalConsumptionForDate(date);
            var sunData = sunDataCacheManager.getOrFetch(date);
            result.add(mergeLists(g,w, holiday, sunData));
        }

        return result;
    }

    private List<EnergySnapshotRecord> mergeLists(
            List<WeatherEntity> weather,
            List<ConsumptionEntity> consumption,
            HolidayEntity holiday,
            SunDataEntity sunData // <-- nuevo
    ) {
        Map<LocalDateTime, WeatherEntity> weatherByHour = weather.stream()
                .collect(Collectors.toMap(
                        w -> w.getTimestamp().toLocalDateTime().withMinute(0).withSecond(0).withNano(0),
                        w -> w,
                        (w1, w2) -> w1
                ));

        return consumption.stream().map(c -> {
            var hour = c.getTimestamp().toLocalDateTime().withMinute(0).withSecond(0).withNano(0);
            WeatherEntity weatherEntity = weatherByHour.get(hour);
            return mergeIntoSnapshot(c, weatherEntity, holiday, sunData);
        }).toList();
    }


    private EnergySnapshotRecord mergeIntoSnapshot(
            ConsumptionEntity consumption,
            WeatherEntity weather,
            HolidayEntity holiday,
            SunDataEntity sunData
    ) {

        return new EnergySnapshotRecord(
                consumption.getTimestamp().toString(),
                consumption.getConsumption(),
                null,
                weather != null ? weather.getTemperature() : null,
                weather != null ? weather.getHumidity() : null,
                weather != null ? weather.getRain() : null,
                weather != null ? weather.getSnow() : null,
                weather != null ? weather.getPressure() : null,
                weather != null ? weather.getWindSpeed() : null,
                weather != null ? weather.getWindDirection() : null,
                weather != null ? weather.getClouds() : null,

                sunData != null ? OffsetDateTime.parse(sunData.getSunrise()) : null,
                sunData != null ? OffsetDateTime.parse(sunData.getSunset()) : null,


                holiday.isWorkingDay(),
                holiday.isHoliday()
        );
    }
}
