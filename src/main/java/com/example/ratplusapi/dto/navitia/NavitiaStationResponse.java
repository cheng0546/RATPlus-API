package com.example.ratplusapi.dto.navitia;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record NavitiaStationResponse(
        @JsonProperty("stop_points")
        List<NavitiaStation> stations
) {
}
