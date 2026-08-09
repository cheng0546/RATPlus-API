package com.example.ratplusapi.service;

import com.example.ratplusapi.client.IdfmClient;
import com.example.ratplusapi.dto.LineDto;
import com.example.ratplusapi.dto.StationDto;
import com.example.ratplusapi.dto.idfm.*;
import com.example.ratplusapi.model.TransportMode;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class LineService {

    private final IdfmClient idfmClient;

    public LineService (IdfmClient idfmClient) {
        this.idfmClient = idfmClient;
    }

    public List<LineDto> getLines() {
        IdfmLinesResponse response = idfmClient.getLines();

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
        IdfmStationResponse response = idfmClient.getStationsFromLineId(lineId);

        return response.stations()
                .stream()
                .collect(Collectors.groupingBy(
                        IdfmStation::name,
                        LinkedHashMap::new,
                        Collectors.toList()
                ))
                .values()
                .stream()
                .map(stopPoints -> new StationDto(
                        stopPoints.getFirst().name(),
                        stopPoints.stream()
                                .map(IdfmStation::id)
                                .toList(),
                        stopPoints.getFirst().label()
                ))
                .toList();
    }

    private TransportMode toTransportMode(IdfmLine line) {

        return line.physicalModes().stream()
                .map(IdfmPhysicalMode::id)
                .map(TransportMode::fromIdfmPhysicalMode)
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);
    }

}
