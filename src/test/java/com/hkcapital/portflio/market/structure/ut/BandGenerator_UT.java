package com.hkcapital.portflio.market.structure.ut;

import com.hkcapital.portflio.market.structure.*;
import com.hkcapital.portflio.model.Instrument;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;
import java.util.NavigableSet;
import java.util.stream.Collectors;

@DisplayName("Test for Generating upper and lower prices Bands for XAU/USD cfd")
class BandGenerator_UT
{
    final String instName = "GOLD";
    final String instTicker = "XAU/USD";
    final String instDesc = "Gold Standard CFD";
    final double maxSlippage = 1.75;
    final boolean active = true;
    final Integer etoroInstId = 18;
    final Instant date = Instant.parse("2026-05-07T23:59:59.00Z");
    final double low = 4689.33;
    final double high = 4749.97;
    final Instrument instrument = Instrument.builder()
            .name(instName)
            .instrumentTicker(instTicker)
            .instrumentDesc(instDesc)
            .maxSlippage(maxSlippage)
            .active(active)
            .etoroInstrumentId(etoroInstId)
            .build();
    final PreviousDayMarketRange
            dayRange = PreviousDayMarketRange.builder()
            .instrument(instrument)
            .date(date)
            .low(low)
            .high(high)
            .build();
    Range range = RangeExtractor.of(dayRange, Modus.builder() //
            .mod(10) //
            .subtract(9) //
            .build());

    @Test
    public void shouldReturnPriceLowerBands()
    {
        NavigableSet<MarketPriceBand> lowerBands = //
                BandGenerator.of(range, BandType.LOW, 10);

        Assertions.assertAll("verify generated lower bands ", () ->
        {
            Assertions.assertNotNull(lowerBands);
            Assertions.assertFalse(lowerBands.isEmpty());
            Assertions.assertEquals(7, lowerBands.size());
            List<MarketPriceBand> bands = lowerBands.stream().collect(Collectors.toList());
            assertPriceBand(BandType.LOW, new BandKey(BandType.LOW, 4680, 4690),
                    4680d, 4690d, 0, bands.get(0));
            assertPriceBand(BandType.LOW, new BandKey(BandType.LOW, 4690, 4700),
                    4690, 4700d, 0, bands.get(1));
            assertPriceBand(BandType.LOW, new BandKey(BandType.LOW, 4700, 4710),
                    4700d, 4710d, 0, bands.get(2));
            assertPriceBand(BandType.LOW, new BandKey(BandType.LOW, 4710, 4720),
                    4710d, 4720d, 0, bands.get(3));
            assertPriceBand(BandType.LOW, new BandKey(BandType.LOW, 4720, 4730),
                    4720d, 4730d, 0, bands.get(4));
            assertPriceBand(BandType.LOW, new BandKey(BandType.LOW, 4730, 4740),
                    4730d, 4740d, 0, bands.get(5));
            assertPriceBand(BandType.LOW, new BandKey(BandType.LOW, 4740, 4750),
                    4740d, 4750d, 0, bands.get(6));
        });
    }


    @Test
    public void shouldReturnPriceUpperrBands()
    {
        NavigableSet<MarketPriceBand> upperBands = //
                BandGenerator.of(range, BandType.HIGH, 10);

        Assertions.assertAll("verify generated lower bands ", () ->
        {
            Assertions.assertNotNull(upperBands);
            Assertions.assertFalse(upperBands.isEmpty());
            Assertions.assertEquals(7, upperBands.size());
            List<MarketPriceBand> bands = upperBands.stream().collect(Collectors.toList());
            bands.forEach(System.out::println);
            assertPriceBand(BandType.HIGH, new BandKey(BandType.HIGH, 4680, 4690),
                    4680d, 4690d, 0, bands.get(0));
            assertPriceBand(BandType.HIGH, new BandKey(BandType.HIGH, 4690, 4700),
                    4690, 4700d, 0, bands.get(1));
            assertPriceBand(BandType.HIGH, new BandKey(BandType.HIGH, 4700, 4710),
                    4700d, 4710d, 0, bands.get(2));
            assertPriceBand(BandType.HIGH, new BandKey(BandType.HIGH, 4710, 4720),
                    4710d, 4720d, 0, bands.get(3));
            assertPriceBand(BandType.HIGH, new BandKey(BandType.HIGH, 4720, 4730),
                    4720d, 4730d, 0, bands.get(4));
            assertPriceBand(BandType.HIGH, new BandKey(BandType.HIGH, 4730, 4740),
                    4730d, 4740d, 0, bands.get(5));
            assertPriceBand(BandType.HIGH, new BandKey(BandType.HIGH, 4740, 4750),
                    4740d, 4750d, 0, bands.get(6));
        });
    }

    private void assertPriceBand(BandType expctedBandType,
                                 BandKey expectedKey,
                                 double expectedLowerBound,
                                 double expectedUpperBound,
                                 int marketVisitCount,
                                 MarketPriceBand marketPriceBand)
    {
        Assertions.assertEquals(expctedBandType, marketPriceBand.getBandType());
        Assertions.assertEquals(expectedKey, marketPriceBand.getBandKey());
        Assertions.assertEquals(expectedLowerBound, marketPriceBand.getLowerBound());
        Assertions.assertEquals(expectedUpperBound, marketPriceBand.getUpperBound());
        Assertions.assertEquals(marketVisitCount, marketPriceBand.getMarketVisitCount());
    }
}