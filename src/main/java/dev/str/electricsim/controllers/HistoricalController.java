package dev.str.electricsim.controllers;

import dev.str.electricsim.entity.ConsumptionEntity;
import dev.str.electricsim.services.HistoricalConsumptionService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
public class HistoricalController {

    private final HistoricalConsumptionService historicalConsumptionService;

    public HistoricalController(HistoricalConsumptionService historicalConsumptionService) {
        this.historicalConsumptionService = historicalConsumptionService;
    }

    @GetMapping
    public List<ConsumptionEntity> getHistoricalConsumption(@RequestParam LocalDate date) {
        if (date == null) {
            return List.of();
        }
        // For demonstration, we use a fixed date. In a real application, this could be a request parameter.
        return historicalConsumptionService.getHistoricalConsumptionForDate(date);
    }
}
