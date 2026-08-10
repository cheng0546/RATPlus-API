package com.example.ratplusapi.controller;

import com.example.ratplusapi.dto.LineDto;
import com.example.ratplusapi.dto.StationDto;
import com.example.ratplusapi.service.LineService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/lines")
public class LineController {

    private final LineService lineService;

    public LineController(LineService lineService) {
        this.lineService = lineService;
    }

    @GetMapping
    public List<LineDto> getLines() {
        return lineService.getLines();
    }

    @GetMapping("/{lineId}/stations")
    public List<StationDto> getStationsFromLineId(@PathVariable String lineId) {
        return lineService.getStationsFromLineId(lineId);
    }

}
