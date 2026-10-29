package com.example.ratplusapi.service;

import com.example.ratplusapi.client.IdfmClient;
import com.example.ratplusapi.dto.DepartureDto;
import com.example.ratplusapi.dto.idfm.IdfmDepartureResponse;
import com.example.ratplusapi.dto.idfm.MonitoredCall;
import com.example.ratplusapi.dto.idfm.MonitoredStopVisit;
import com.example.ratplusapi.dto.idfm.MonitoredVehicleJourney;
import com.example.ratplusapi.dto.navitia.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.Comparator;
import java.util.List;

@Service
public class DepartureService {

    private final IdfmClient idfmClient;

    public DepartureService(IdfmClient idfmClient) {
        this.idfmClient = idfmClient;
    }

    public List<DepartureDto> getNextDepartures(String lineId, List<String> stationIds) {
        String lineRef = toLineRef(lineId);

        return stationIds.stream()
                .flatMap(stationId -> fetchDepartures(stationId, lineRef).stream())
                .sorted(Comparator.comparing(DepartureDto::arrivalTime))
                .toList();
    }

    // Metro/tram use StopPoint refs; RER/Transilien trains may only answer on StopArea refs
    private List<DepartureDto> fetchDepartures(String stationId, String lineRef) {
        String id = stationId.substring(stationId.lastIndexOf(":") + 1);

        List<String> monitoringRefs = List.of(
                "STIF:StopPoint:Q:" + id + ":",
                "STIF:StopArea:SP:" + id + ":"
        );

        for (String monitoringRef : monitoringRefs) {
            try {
                List<DepartureDto> departures = toDepartureDtos(
                        idfmClient.getNextDepartures(monitoringRef, lineRef)
                );
                if (!departures.isEmpty()) {
                    return departures;
                }
            } catch (RestClientException e) {
                // try next reference format
            }
        }

        return List.of();
    }

    private String toLineRef(String lineId) {
        String id = lineId.substring(lineId.lastIndexOf(":") + 1);

        return "STIF:Line::" + id + ":";
    }

    private List<DepartureDto> toDepartureDtos(IdfmDepartureResponse response) {
        return response.siri()
                .serviceDelivery()
                .stopMonitoringDelivery()
                .stream()
                .flatMap(delivery -> delivery.monitoredStopVisit().stream())
                .map(this::toDepartureDto)
                .toList();
    }

    private String firstValue(List<com.example.ratplusapi.dto.idfm.Value> values) {
        return values == null || values.isEmpty() ? null : values.getFirst().value();
    }

    private DepartureDto toDepartureDto(MonitoredStopVisit visit) {

        MonitoredVehicleJourney journey = visit.monitoredVehicleJourney();
        MonitoredCall call = journey.monitoredCall();

        ZoneId parisZone = ZoneId.of("Europe/Paris");

        // Trains often provide only one of the two times
        String arrival = call.expectedArrivalTime() != null
                ? call.expectedArrivalTime() : call.expectedDepartureTime();
        String departure = call.expectedDepartureTime() != null
                ? call.expectedDepartureTime() : call.expectedArrivalTime();

        LocalDateTime arrivalTime = OffsetDateTime
                .parse(arrival)
                .atZoneSameInstant(parisZone)
                .toLocalDateTime();

        LocalDateTime departureTime = OffsetDateTime
                .parse(departure)
                .atZoneSameInstant(parisZone)
                .toLocalDateTime();

        return new DepartureDto(
                firstValue(journey.directionName()),
                firstValue(journey.destinationName()),
                arrivalTime,
                departureTime,
                call.departureStatus()
        );
    }

}
