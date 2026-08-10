package com.example.ratplusapi.model;

import java.util.Arrays;

public enum TransportMode {
    METRO("physical_mode:Metro"),
    RER("physical_mode:RapidTransit"),
    TRAM("physical_mode:Tramway"),
    TRANSILIEN("physical_mode:LocalTrain");

    private final String navitiaPhysicalMode;

    TransportMode(String navitiaPhysicalMode) {
        this.navitiaPhysicalMode = navitiaPhysicalMode;
    }

    public static TransportMode fromNavitiaPhysicalMode(String navitiaPhysicalMode) {
        return Arrays.stream(values())
                .filter(mode -> mode.navitiaPhysicalMode.equals(navitiaPhysicalMode))
                .findFirst()
                .orElse(null);
    }
}
