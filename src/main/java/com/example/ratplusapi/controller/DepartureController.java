package com.example.ratplusapi.controller;

import com.example.ratplusapi.dto.DepartureDto;
import com.example.ratplusapi.service.DepartureService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/departures")
public class DepartureController {

    private final DepartureService departureService;

    public DepartureController(DepartureService departureService) {
        this.departureService = departureService;
    }

    @GetMapping
    public List<DepartureDto> getNextDepartures(@RequestParam String lineId,
                                                @RequestParam List<String> stationIds) {
        return departureService.getNextDepartures(lineId, stationIds);
    }

}
