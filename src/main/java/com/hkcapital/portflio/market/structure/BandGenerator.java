package com.hkcapital.portflio.market.structure;

import com.hkcapital.portflio.market.indicators.TimeFramesUnit;

import java.time.Instant;
import java.util.NavigableSet;
import java.util.TreeSet;

public class BandGenerator
{
    public static NavigableSet<MarketPriceBand> of(Range range, BandType bandType, //
                                                   int interval, Integer timeFrame, //
                                                   TimeFramesUnit timeFrameUnit)
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
                            .initialVisitedTime(Instant.now())
                            .lastVisitedTime(Instant.now())
                            .upperBound(upperBound)
                            .timeFrame(timeFrame)
                            .timeFrameUnit(timeFrameUnit)
                            .build();
            priceBands.add(marketPriceBand);
        }
        return priceBands;
    }
}
