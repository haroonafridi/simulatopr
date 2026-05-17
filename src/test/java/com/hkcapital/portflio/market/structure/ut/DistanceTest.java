package com.hkcapital.portflio.market.structure.ut;

import com.hkcapital.portflio.market.structure.BandKey;
import com.hkcapital.portflio.market.structure.BandType;
import com.hkcapital.portflio.market.structure.Distance;
import com.hkcapital.portflio.market.structure.MarketPriceBand;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.util.Assert;

import static org.junit.jupiter.api.Assertions.*;

class DistanceTest
{

    @Test
    void shouldCalculateDistanceBetweenBands()
    {

        int ubh1 = 4730;

        int lbh1 = 4720;

        Double price = 4690d;

        int ubl1 =4690;

        int lbl1 = 4670;

        MarketPriceBand high = MarketPriceBand
                .builder()
                .upperBound(Double.valueOf(ubh1))
                .lowerBound(Double.valueOf(lbh1))
                .bandKey(new BandKey(BandType.HIGH, ubh1, lbh1))
                .bandType(BandType.HIGH).build();


        MarketPriceBand low = MarketPriceBand
                .builder()
                .upperBound(Double.valueOf(ubl1))
                .lowerBound(Double.valueOf(lbl1))
                .bandKey(new BandKey(BandType.LOW, ubl1, lbl1))
                .bandType(BandType.LOW).build();

        Distance dFromHigh = Distance.builder()
                .referencePrice(high.getUpperBound())
                .currentPrice(price)
                .build();


        Distance dFromLow = Distance.builder()
                .referencePrice(low.getLowerBound())
                .currentPrice(price)
                .build();

        Assertions.assertEquals(40d, Math.abs(dFromHigh.absPoints()));

        Assertions.assertEquals(
                0.8456d,
                dFromHigh.absPercent(),
                0.0001, "Price is near short term resistance"
        );

        Assertions.assertEquals(20d, Math.abs(dFromLow.absPoints()));

        Assertions.assertEquals(
                0.4282d,
                Math.abs(dFromLow.absPercent()),
                0.0001, "Price is near short term support"
        );

    }
}