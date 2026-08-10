package com.example.ratplusapi.dto.idfm;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record MonitoredVehicleJourney(
        @JsonProperty("DirectionName")
        List<Value> directionName,

        @JsonProperty("DestinationName")
        List<Value> destinationName,

        @JsonProperty("MonitoredCall")
        MonitoredCall monitoredCall
) {}
