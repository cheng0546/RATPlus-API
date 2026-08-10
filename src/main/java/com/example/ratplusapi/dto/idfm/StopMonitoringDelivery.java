package com.example.ratplusapi.dto.idfm;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record StopMonitoringDelivery(
        @JsonProperty("MonitoredStopVisit")
        List<MonitoredStopVisit> monitoredStopVisit
) {}
