package com.hkcapital.portflio.market.structure;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public record BandKey(
        BandType bandType,
        int lowerBound,
        int upperBound) {

    @JsonValue
    @Override
    public String toString() {
        return bandType.name().toLowerCase() +
                "_" +
                lowerBound +
                "_" +
                upperBound;
    }

    @JsonCreator
    public static BandKey fromString(String value) {
        String[] parts = value.split("_");

        BandType type = BandType.valueOf(parts[0].toUpperCase());
        int lower = Integer.parseInt(parts[1]);
        int upper = Integer.parseInt(parts[2]);

        return new BandKey(type, lower, upper);
    }
}