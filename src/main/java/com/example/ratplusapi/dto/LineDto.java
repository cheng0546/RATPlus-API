package com.example.ratplusapi.dto;

import com.example.ratplusapi.model.TransportMode;

public record LineDto(
        String id,
        String name,
        TransportMode mode
) {
}
