package com.example.ratplusapi.dto.idfm;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record ServiceDelivery(
        @JsonProperty("StopMonitoringDelivery")
        List<StopMonitoringDelivery> stopMonitoringDelivery
) {}
