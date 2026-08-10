package com.example.ratplusapi.service;

import com.example.ratplusapi.client.IdfmClient;
import com.example.ratplusapi.dto.DepartureDto;
import com.example.ratplusapi.dto.idfm.IdfmDepartureResponse;
import com.example.ratplusapi.dto.idfm.MonitoredCall;
import com.example.ratplusapi.dto.idfm.MonitoredStopVisit;
import com.example.ratplusapi.dto.idfm.MonitoredVehicleJourney;
import com.example.ratplusapi.dto.navitia.*;
import org.springframework.stereotype.Service;

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
                .map(this::toMonitoringRef)
                .map(monitoringRef ->
                        idfmClient.getNextDepartures(
                                monitoringRef,
                                lineRef
                        )
                )
                .flatMap(response -> toDepartureDtos(response).stream())
                .sorted(Comparator.comparing(DepartureDto::arrivalTime))
                .toList();
    }

    private String toMonitoringRef(String stopPointId) {
        String id = stopPointId.substring(stopPointId.lastIndexOf(":") + 1);

        return "STIF:StopPoint:Q:" + id + ":";
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

    private DepartureDto toDepartureDto(MonitoredStopVisit visit) {

        MonitoredVehicleJourney journey = visit.monitoredVehicleJourney();
        MonitoredCall call = journey.monitoredCall();

        ZoneId parisZone = ZoneId.of("Europe/Paris");

        LocalDateTime arrivalTime = OffsetDateTime
                .parse(call.expectedArrivalTime())
                .atZoneSameInstant(parisZone)
                .toLocalDateTime();

        LocalDateTime departureTime = OffsetDateTime
                .parse(call.expectedDepartureTime())
                .atZoneSameInstant(parisZone)
                .toLocalDateTime();

        return new DepartureDto(
                journey.directionName().getFirst().value(),
                journey.destinationName().getFirst().value(),
                arrivalTime,
                departureTime,
                call.departureStatus()
        );
    }

}
