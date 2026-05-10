package com.hkcapital.portflio.market.structure;

public class RangeExtractor
{
    public static Range of(PriceRange dayRange, Modus modus) //
    {
        double low = (dayRange.getLow() - dayRange.getLow() % modus.getMod());
        double high = dayRange.getHigh() + (modus.getSubtract() - dayRange.getHigh() % modus.getMod());
        return Range.builder() //
                .low(low) //
                .high(high) //
                .build();
    }
}
