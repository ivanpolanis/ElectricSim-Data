package dev.str.electricsim.services;

import dev.str.electricsim.client.SunriseSunsetClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class HistoricalSunDataService {
    private static final Logger log = LoggerFactory.getLogger(HistoricalSunDataService.class);

    private final SunriseSunsetClient sunriseSunsetClient;

    public HistoricalSunDataService(SunriseSunsetClient sunriseSunsetClient) {
        this.sunriseSunsetClient = sunriseSunsetClient;
    }

    /**
     * Obtiene los datos históricos de sunrise/sunset entre dos fechas, inclusive.
     */
    public List<Map<String, Object>> getHistoricalSunDataBetweenDates(LocalDate start, LocalDate end) {
        List<Map<String, Object>> results = new ArrayList<>();

        for (LocalDate date = start; !date.isAfter(end); date = date.plusDays(1)) {
            log.info("Fetching sunrise/sunset data for {}", date);

            Map<String, Object> sunData = sunriseSunsetClient.getSunData(date);
            if (sunData != null && !sunData.isEmpty()) {
                results.add(sunData);
            } else {
                log.warn("No data returned for {}", date);
            }
        }

        return results;
    }
}
