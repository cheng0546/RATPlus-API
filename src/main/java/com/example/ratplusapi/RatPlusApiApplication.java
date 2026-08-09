package com.example.ratplusapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class RatPlusApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(RatPlusApiApplication.class, args);
    }

}
