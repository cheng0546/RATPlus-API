package com.example.ratplusapi.service;

import com.example.ratplusapi.client.NavitiaClient;
import com.example.ratplusapi.dto.LineDto;
import com.example.ratplusapi.dto.StationDto;
import com.example.ratplusapi.dto.navitia.*;
import com.example.ratplusapi.model.TransportMode;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class LineService {

    private final NavitiaClient navitiaClient;

    public LineService (NavitiaClient navitiaClient) {
        this.navitiaClient = navitiaClient;
    }

    public List<LineDto> getLines() {
        NavitiaLinesResponse response = navitiaClient.getLines();

        return response.lines()
                .stream()
                .map(line -> new LineDto(
                        line.id(),
                        line.name(),
                        toTransportMode(line)
                ))
                .toList();
    }

    public List<StationDto> getStationsFromLineId(String lineId) {
        NavitiaStationResponse response = navitiaClient.getStationsFromLineId(lineId);

        return response.stations()
                .stream()
                .collect(Collectors.groupingBy(
                        NavitiaStation::name,
                        LinkedHashMap::new,
                        Collectors.toList()
                ))
                .values()
                .stream()
                .map(stopPoints -> new StationDto(
                        stopPoints.getFirst().name(),
                        stopPoints.stream()
                                .map(NavitiaStation::id)
                                .toList(),
                        stopPoints.getFirst().label()
                ))
                .toList();
    }

    private TransportMode toTransportMode(NavitiaLine line) {

        return line.physicalModes().stream()
                .map(NavitiaPhysicalMode::id)
                .map(TransportMode::fromNavitiaPhysicalMode)
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);
    }

}
