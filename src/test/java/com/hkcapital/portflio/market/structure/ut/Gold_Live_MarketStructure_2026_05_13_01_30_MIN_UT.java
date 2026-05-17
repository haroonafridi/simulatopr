package com.hkcapital.portflio.market.structure.ut;

import com.hkcapital.portflio.market.structure.Distance;
import com.hkcapital.portflio.market.structure.MarketPriceBand;
import com.hkcapital.portflio.market.structure.MarketStructureMap;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.NavigableSet;
import java.util.Optional;

public class Gold_Live_MarketStructure_2026_05_13_01_30_MIN_UT extends BandAbstract_UT
{
    @Test
    public void shouldBuyLowSellHigh() throws IOException
    {
        NavigableSet<MarketPriceBand> bands =
                from("/data/portfolio/market/structure/gold_15_mins/2026-05-13_01_45.json");

        double ask = 4717.2;

        MarketStructureMap signal = new MarketStructureMap(bands, ask);


        Optional<MarketPriceBand> firstMostVisitedHighBand = signal.findNthMostVisitedHighBand(0);

        Optional<MarketPriceBand> secondMostVisitedHighBand = signal.findNthMostVisitedHighBand(1);

        Optional<MarketPriceBand> firstMostVisitedLowBand = signal.findNthMostVisitedLowBand(0);

        Optional<MarketPriceBand> secondMostVisitedLowBand = signal.findNthMostVisitedLowBand(1);

        Distance denseUpperRejectionArea1 =  Distance.builder()
                .referencePrice(secondMostVisitedHighBand.get().getUpperBound())
                .currentPrice(ask).build();

        Distance denseUpperRejectionArea2 =  Distance.builder()
                .referencePrice(firstMostVisitedHighBand.get().getUpperBound())
                .currentPrice(ask).build();


        Distance denseLowerRejectionArea1 =  Distance.builder()
                .referencePrice(firstMostVisitedLowBand.get().getLowerBound())
                .currentPrice(ask).build();

        Distance denseLowerRejectionArea2 =  Distance.builder()
                .referencePrice(secondMostVisitedLowBand.get().getLowerBound())
                .currentPrice(ask).build();

    }
}
