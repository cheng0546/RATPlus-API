package com.example.ratplusapi.service;

import com.example.ratplusapi.client.IdfmClient;
import com.example.ratplusapi.dto.LineDto;
import com.example.ratplusapi.dto.idfm.IdfmLine;
import com.example.ratplusapi.dto.idfm.IdfmLinesResponse;
import com.example.ratplusapi.dto.idfm.IdfmPhysicalMode;
import com.example.ratplusapi.model.TransportMode;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

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

    private TransportMode toTransportMode(IdfmLine line) {

        return line.physicalModes().stream()
                .map(IdfmPhysicalMode::id)
                .map(TransportMode::fromIdfmPhysicalMode)
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);
    }

}
