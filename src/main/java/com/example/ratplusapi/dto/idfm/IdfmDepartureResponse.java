package com.example.ratplusapi.dto.idfm;

import com.fasterxml.jackson.annotation.JsonProperty;

public record IdfmDepartureResponse(
        @JsonProperty("Siri")
        Siri siri
) {}

