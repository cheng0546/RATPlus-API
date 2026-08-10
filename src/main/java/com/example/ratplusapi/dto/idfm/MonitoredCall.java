package com.example.ratplusapi.dto.idfm;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record MonitoredCall(
        @JsonProperty("StopPointName")
        List<Value> stopPointName,

        @JsonProperty("ExpectedArrivalTime")
        String expectedArrivalTime,

        @JsonProperty("ExpectedDepartureTime")
        String expectedDepartureTime,

        @JsonProperty("DepartureStatus")
        String departureStatus,

        @JsonProperty("ArrivalStatus")
        String arrivalStatus
) {}
