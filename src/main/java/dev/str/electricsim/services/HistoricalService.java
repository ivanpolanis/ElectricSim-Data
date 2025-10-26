package dev.str.electricsim.services;

import dev.str.electricsim.entity.ConsumptionEntity;
import dev.str.electricsim.entity.EnergySnapshot;
import dev.str.electricsim.entity.HolidayEntity;
import dev.str.electricsim.entity.WeatherEntity;
import dev.str.electricsim.model.EnergySnapshotRecord;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class HistoricalService {
    private final HolidayService holidayService;
    private final HistoricalWeatherService historicalWeatherService;
    private final HistoricalConsumptionService historicalConsumptionService;

    public HistoricalService(HolidayService holidayService,HistoricalWeatherService historicalWeatherService, HistoricalConsumptionService historicalConsumptionService) {
        this.holidayService = holidayService;
        this.historicalWeatherService = historicalWeatherService;
        this.historicalConsumptionService = historicalConsumptionService;
    }

    public List<List<EnergySnapshotRecord>> getHistoricalWeatherBetweenDates(LocalDate start, LocalDate end) {
        List<List<EnergySnapshotRecord>> result = new java.util.ArrayList<>();
        for (LocalDate date = start; !date.isAfter(end); date = date.plusDays(1)) {
            var holiday = holidayService.getHolidayStatusForDate(date);
            var g = historicalWeatherService.getHistoricalWeatherForDate(date);
            var w = historicalConsumptionService.getHistoricalConsumptionForDate(date);
            result.add(mergeLists(g,w, holiday));
        }

        return result;
    }

    private List<EnergySnapshotRecord> mergeLists(List<WeatherEntity> weather, List<ConsumptionEntity> consumption, HolidayEntity holiday) {
        Map<LocalDateTime, WeatherEntity> weatherByHour = weather.stream()
                .collect(Collectors.toMap(
                        w -> w.getTimestamp().toLocalDateTime().withMinute(0).withSecond(0).withNano(0),
                        w -> w,
                        (w1, w2) -> w1 // In case of duplicates, keep the first one
                ));

        return consumption.stream().map(c -> {
            var hour = c.getTimestamp().toLocalDateTime().withMinute(0).withSecond(0).withNano(0);
            WeatherEntity weatherEntity = weatherByHour.get(hour);
            return mergeIntoSnapshot(c, weatherEntity, holiday);
        }).toList();
    }

    private EnergySnapshotRecord mergeIntoSnapshot(ConsumptionEntity consumption, WeatherEntity weather, HolidayEntity holiday) {
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

                null,
                null,
                holiday.isWorkingDay(),
                holiday.isHoliday()
        );
    }
}
