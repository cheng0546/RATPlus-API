package com.example.ratplusapi.client;

import com.example.ratplusapi.config.IdfmProperties;
import com.example.ratplusapi.dto.idfm.IdfmLinesResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class IdfmClient {

    private final RestClient restClient;

    private static final String LINES_FILTER =
            "physical_mode.id=physical_mode:Metro"
                    + " or physical_mode.id=physical_mode:RapidTransit"
                    + " or physical_mode.id=physical_mode:Tramway"
                    + " or physical_mode.id=physical_mode:LocalTrain";

    public IdfmClient(IdfmProperties idfmProperties) {
        this.restClient = RestClient.builder()
                .baseUrl(idfmProperties.baseUrl())
                .defaultHeader("apikey", idfmProperties.token())
                .build();
    }

    public IdfmLinesResponse getLines() {
        return restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/navitia/lines")
                        .queryParam("count", 100)
                        .queryParam("filter", LINES_FILTER)
                        .build())
                .retrieve()
                .body(IdfmLinesResponse.class);
    }
}
