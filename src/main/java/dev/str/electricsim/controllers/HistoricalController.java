package dev.str.electricsim.controllers;

import dev.str.electricsim.model.EnergySnapshotRecord;
import dev.str.electricsim.services.HistoricalService;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;

@RestController
public class HistoricalController {

    private final HistoricalService historicalService;

    public HistoricalController(HistoricalService historicalService) {
        this.historicalService = historicalService;
    }

    @GetMapping
    public ResponseEntity<Resource> getHistoricalConsumption(@RequestParam LocalDate start, @RequestParam LocalDate end) {
        if (start == null || end == null || start.isAfter(end)) {
            return ResponseEntity.internalServerError().build();
        }
        var data = historicalService.getHistoricalWeatherBetweenDates(start, end);

        try {
            String csvContent = generateCsv(data);

            byte[] csvBytes = csvContent.getBytes(StandardCharsets.UTF_8);
            InputStreamResource resource = new InputStreamResource(new ByteArrayInputStream(csvBytes));

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"reporte_commons.csv\"");

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentLength(csvBytes.length)
                    .contentType(MediaType.parseMediaType("text/csv; charset=utf-8"))
                    .body(resource);

        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    private String generateCsv(List<List<EnergySnapshotRecord>> data) throws IOException {
        List<EnergySnapshotRecord> flattened = data.stream()
                .flatMap(List::stream)
                .toList();

        String[] headers = {"date", "consumption", "temperature", "humidity", "rain", "snow", "pressure", "wind_speed", "wind_direction", "clouds", "sunrise", "sunset", "working_day", "holiday"};


        CSVFormat format = CSVFormat.DEFAULT.builder()
                .setHeader(headers)
                .build();

        StringWriter sw = new StringWriter();
        try (CSVPrinter printer = new CSVPrinter(sw, format)) {
            for (EnergySnapshotRecord record : flattened) {
                printer.printRecord(record.toCsvRecord());
            }
            printer.flush();
        }

        return sw.toString();
    }
}
