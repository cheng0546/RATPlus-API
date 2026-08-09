package com.example.ratplusapi.dto.idfm;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record IdfmLine(
        String id,
        String name,
        String code,
        String color,

        @JsonProperty("text_color")
        String textColor,

        @JsonProperty("physical_modes")
        List<IdfmPhysicalMode> physicalModes
) {
}
