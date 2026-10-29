package com.example.ratplusapi.client;

import com.example.ratplusapi.config.IdfmProperties;
import com.example.ratplusapi.dto.idfm.IdfmDepartureResponse;
import org.springframework.stereotype.Component;
import tools.jackson.databind.json.JsonMapper;
import org.springframework.web.client.RestClient;

@Component
public class IdfmClient {

    private final RestClient restClient;
    private final JsonMapper jsonMapper = JsonMapper.builder().build();

    public IdfmClient(IdfmProperties idfmProperties) {
        this.restClient = RestClient.builder()
                .baseUrl(idfmProperties.baseUrl())
                .defaultHeader("apikey", idfmProperties.token())
                .build();
    }

    public IdfmDepartureResponse getNextDepartures(String monitoringRef, String lineRef) {
        byte[] body = restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/stop-monitoring")
                        .queryParam("MonitoringRef", monitoringRef)
                        .queryParam("LineRef", lineRef)
                        .build())
                .retrieve()
                .body(byte[].class);

        // The server may declare a wrong charset; JSON bytes are decoded as UTF-8 by Jackson
        return jsonMapper.readValue(body, IdfmDepartureResponse.class);
    }

}
