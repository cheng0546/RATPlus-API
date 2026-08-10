package com.example.ratplusapi.client;

import com.example.ratplusapi.config.IdfmProperties;
import com.example.ratplusapi.dto.navitia.NavitiaLinesResponse;
import com.example.ratplusapi.dto.navitia.NavitiaStationResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class NavitiaClient {

    private final RestClient restClient;

    private static final String LINES_FILTER =
            "physical_mode.id=physical_mode:Metro"
                    + " or physical_mode.id=physical_mode:RapidTransit"
                    + " or physical_mode.id=physical_mode:Tramway"
                    + " or physical_mode.id=physical_mode:LocalTrain";

    public NavitiaClient(IdfmProperties idfmProperties) {
        this.restClient = RestClient.builder()
                .baseUrl(idfmProperties.navitiaBaseUrl())
                .defaultHeader("apikey", idfmProperties.token())
                .build();
    }

    public NavitiaLinesResponse getLines() {
        return restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/navitia/lines")
                        .queryParam("count", 100)
                        .queryParam("filter", LINES_FILTER)
                        .build())
                .retrieve()
                .body(NavitiaLinesResponse.class);
    }

    public NavitiaStationResponse getStationsFromLineId(String lineId) {
        return restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/navitia/lines/" + lineId + "/stop_points")
                        .queryParam("count", 100)
                        .build()
                )
                .retrieve()
                .body(NavitiaStationResponse.class);
    }
}
