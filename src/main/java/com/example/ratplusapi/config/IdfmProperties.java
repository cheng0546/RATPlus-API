package com.example.ratplusapi.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "idfm")
public record IdfmProperties (
        String navitiaBaseUrl,
        String baseUrl,
        String token
) {
}
