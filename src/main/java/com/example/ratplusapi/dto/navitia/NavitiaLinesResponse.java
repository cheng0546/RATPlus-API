package com.example.ratplusapi.dto.navitia;

import java.util.List;

public record NavitiaLinesResponse(
        List<NavitiaLine> lines
) {
}
