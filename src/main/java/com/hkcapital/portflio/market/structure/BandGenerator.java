package com.hkcapital.portflio.market.structure;

import java.time.Instant;
import java.util.NavigableSet;
import java.util.TreeSet;

public class BandGenerator
{
    public static NavigableSet<MarketPriceBand> of(Range range, BandType bandType, //
                                                   int interval)
    {
        double upper = range.getHigh();
        double lower = range.getLow();
        int diff = (int) Math.ceil((upper - lower) / interval);
        double band = lower;
        int bandCount = 0;

        NavigableSet<MarketPriceBand> priceBands = new TreeSet<>();

        while (bandCount < diff)
        {
            band = band + interval;
            double lowerBound = (band - interval);
            double upperBound = band;
            bandCount++;
            MarketPriceBand marketPriceBand =
                    MarketPriceBand.builder().bandType(bandType)
                            .bandKey(new BandKey(bandType, (int) lowerBound, (int) upperBound))
                            .marketVisitCount(0)
                            .lowerBound(lowerBound)
                            .time(Instant.now())
                            .upperBound(upperBound).build();
            priceBands.add(marketPriceBand);
        }
        return priceBands;
    }
}
