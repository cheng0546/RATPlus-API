package com.example.ratplusapi.dto;

import java.time.LocalDateTime;

public record DepartureDto(
        String direction,
        String destination,
        LocalDateTime arrivalTime,
        LocalDateTime departureTime,
        String status
) {
}
