package com.example.ratplusapi.dto;

import java.util.List;

public record StationDto(
        String name,
        List<String> ids,
        String label
) {
}
