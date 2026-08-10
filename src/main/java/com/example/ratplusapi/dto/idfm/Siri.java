package com.example.ratplusapi.dto.idfm;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Siri(
        @JsonProperty("ServiceDelivery")
        ServiceDelivery serviceDelivery
) {
}
