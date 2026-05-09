package com.hkcapital.portflio.market.structure;

public record BandKey(
        BandType bandType,
        int lowerBound,
        int upperBound)
{
    @Override
    public String toString()
    {
        return bandType.name().toLowerCase() +
                "_" +
                lowerBound +
                "_" +
                upperBound;
    }
}