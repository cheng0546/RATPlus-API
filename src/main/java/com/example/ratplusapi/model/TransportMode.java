package com.example.ratplusapi.model;

import java.util.Arrays;

public enum TransportMode {
    METRO("physical_mode:Metro"),
    RER("physical_mode:RapidTransit"),
    TRAM("physical_mode:Tramway"),
    TRANSILIEN("physical_mode:LocalTrain");

    private final String idfmPhysicalMode;

    TransportMode(String idfmPhysicalMode) {
        this.idfmPhysicalMode = idfmPhysicalMode;
    }

    public static TransportMode fromIdfmPhysicalMode(String idfmPhysicalMode) {
        return Arrays.stream(values())
                .filter(mode -> mode.idfmPhysicalMode.equals(idfmPhysicalMode))
                .findFirst()
                .orElse(null);
    }
}
