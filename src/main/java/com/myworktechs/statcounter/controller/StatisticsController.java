package com.myworktechs.statcounter.controller;

import com.myworktechs.statcounter.controller.dto.ErrorResponse;
import com.myworktechs.statcounter.service.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.regex.Pattern;

@RestController
public class StatisticsController {

    private static final Pattern COUNTRY_PATTERN = Pattern.compile("^[A-Za-z]{2}$");
    private final StatisticsService statisticsService;

    @Autowired
    public StatisticsController(StatisticsService statisticsService) {
        this.statisticsService = statisticsService;
    }

    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    public ResponseEntity<?> updateStatistics(@RequestParam String country) {
        if (COUNTRY_PATTERN.matcher(country).matches()) {
            statisticsService.updateCountry(country);
            return ResponseEntity
                    .ok()
                    .build();
        } else
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(String.format("Invalid country name: %s", country)));
    }


    @RequestMapping(value = "/report", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Long> reportStatistics() {
        return statisticsService.report();
    }
}