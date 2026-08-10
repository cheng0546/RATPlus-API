package com.example.ratplusapi.dto.navitia;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record NavitiaLine(
        String id,
        String name,
        String code,
        String color,

        @JsonProperty("text_color")
        String textColor,

        @JsonProperty("physical_modes")
        List<NavitiaPhysicalMode> physicalModes
) {
}
