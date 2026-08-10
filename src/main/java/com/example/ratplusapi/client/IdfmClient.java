package com.example.ratplusapi.client;

import com.example.ratplusapi.config.IdfmProperties;
import com.example.ratplusapi.dto.idfm.IdfmDepartureResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class IdfmClient {

    private final RestClient restClient;

    public IdfmClient(IdfmProperties idfmProperties) {
        this.restClient = RestClient.builder()
                .baseUrl(idfmProperties.baseUrl())
                .defaultHeader("apikey", idfmProperties.token())
                .build();
    }

    public IdfmDepartureResponse getNextDepartures(String monitoringRef, String lineRef) {
        return restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/stop-monitoring")
                        .queryParam("MonitoringRef", monitoringRef)
                        .queryParam("LineRef", lineRef)
                        .build())
                .retrieve()
                .body(IdfmDepartureResponse.class);
    }

}
