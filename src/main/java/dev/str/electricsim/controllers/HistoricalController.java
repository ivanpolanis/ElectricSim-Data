package dev.str.electricsim.controllers;

import dev.str.electricsim.entity.ConsumptionEntity;
import dev.str.electricsim.model.EnergySnapshotRecord;
import dev.str.electricsim.services.HistoricalConsumptionService;
import dev.str.electricsim.services.HistoricalService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
public class HistoricalController {

    private final HistoricalService historicalService;

    public HistoricalController(HistoricalService historicalService) {
        this.historicalService = historicalService;
    }

    @GetMapping
    public List<List<EnergySnapshotRecord>> getHistoricalConsumption(@RequestParam LocalDate start, @RequestParam LocalDate end) {
        if (start == null || end == null || start.isAfter(end)) {
            return List.of();
        }
        // For demonstration, we use a fixed date. In a real application, this could be a request parameter.
        return historicalService.getHistoricalWeatherBetweenDates(start, end);
    }
}
