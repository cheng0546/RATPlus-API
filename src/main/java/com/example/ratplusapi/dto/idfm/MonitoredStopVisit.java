package com.example.ratplusapi.dto.idfm;

import com.fasterxml.jackson.annotation.JsonProperty;

public record MonitoredStopVisit(
        @JsonProperty("MonitoredVehicleJourney")
        MonitoredVehicleJourney monitoredVehicleJourney
) {}
