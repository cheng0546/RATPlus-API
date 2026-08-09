package com.example.ratplusapi.dto.idfm;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record IdfmStationResponse(
        @JsonProperty("stop_points")
        List<IdfmStation> stations
) {
}
