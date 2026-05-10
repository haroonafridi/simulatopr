package com.hkcapital.portflio.market.structure;

import com.hkcapital.portflio.model.Candle;
import com.hkcapital.portflio.model.Instrument;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.NavigableSet;
import java.util.TreeSet;

class Gold_MarketStructure_UT
{
    @Test
    public void shouldCreateGoldMarketStructure() throws IOException
    {
        final String instName = "GOLD";
        final String instTicker = "XAU/USD";
        final String instDesc = "Gold Standard CFD";
        final double maxSlippage = 1.75;
        final boolean active = true;
        final Integer etoroInstId = 18;
        final Instant date = Instant.parse("2026-05-07T23:59:59.00Z");
        final double low = 4686;
        final double high = 4765.36;

        final Instrument instrument = Instrument.builder()
                .name(instName)
                .instrumentTicker(instTicker)
                .instrumentDesc(instDesc)
                .maxSlippage(maxSlippage)
                .active(active)
                .etoroInstrumentId(etoroInstId)
                .build();

        final PreviousDayMarketRange
                range = PreviousDayMarketRange.builder()
                .instrument(instrument)
                .date(date)
                .low(low)
                .high(high)
                .build();

        MarketStructure marketStructure = //
                MarketStructure
                        .builder()
                        .intervals(10)
                        .marketSession(MarketSession.builder().build())
                        .modus(Modus.builder().mod(10)
                                .subtract(10).build())
                        .priceRange(range)
                        .build();

        List<String> rows = Files.readAllLines(Path.of("D:/gold_data/gold_candle_07_05_2026.csv"));
        List<Candle> candles = new ArrayList<>();
        for (String row : rows)
        {
            String[] cells = row.split(",");
            candles.add(Candle.builder() //
                    .high(Double.parseDouble(cells[0])) //
                    .low(Double.parseDouble(cells[1])) //
                    .build());
        }
        marketStructure.init(candles);
        assertBandsAfterNewCandleArrival("Assert lower bands ", marketStructure.getLowerBands(), buildExpectedInitialLowerBands());

        assertBandsAfterNewCandleArrival("Assert upper bands ", marketStructure.getUpperBands(), buildExpectedInitialUpperBands());

        Candle newCandle0 = Candle
                .builder()
                .low(4775)
                .high(4800).build();
        marketStructure.updateBands(newCandle0);

        Candle newCandle1 = Candle
                .builder()
                .low(4775)
                .high(4789)
                .build();
        marketStructure.updateBands(newCandle1);

        Candle newCandle2 = Candle
                .builder()
                .low(4660)
                .high(4689)
                .build();
        marketStructure.updateBands(newCandle2);

        assertBandsAfterNewCandleArrival("Assert upper bands after adding new bands", marketStructure.getUpperBands(), buildExpectedModifiedUpperBands());
        assertBandsAfterNewCandleArrival("Assert lower bands after adding new bands", marketStructure.getLowerBands(), buildModifiedExpectedLowerBands());
    }

    private void assertBandsAfterNewCandleArrival(final String heading, final NavigableSet<MarketPriceBand> actualBands,
                                                  final NavigableSet<MarketPriceBand> expectedBands)
    {
        Assertions.assertAll(heading, () ->
        {
            final List<MarketPriceBand> bands = //
                    actualBands.stream().toList();
            Assertions.assertNotNull(bands);
            Assertions.assertFalse(bands.isEmpty());
            Assertions.assertArrayEquals(expectedBands.toArray(), bands.toArray());
        });
    }

    private NavigableSet<MarketPriceBand> buildModifiedExpectedLowerBands()
    {
        MarketPriceBand hb0 = MarketPriceBand
                .builder()
                .bandKey(new BandKey(BandType.LOW, 4660, 4670))
                .bandType(BandType.LOW)
                .lowerBound(4660d)
                .upperBound(4670d)
                .marketVisitCount(1)
                .build();

        MarketPriceBand hb1 = MarketPriceBand
                .builder()
                .bandKey(new BandKey(BandType.LOW, 4670, 4680))
                .bandType(BandType.LOW)
                .lowerBound(4670d)
                .upperBound(4680d)
                .marketVisitCount(0)
                .build();

        MarketPriceBand hb2 = MarketPriceBand
                .builder()
                .bandKey(new BandKey(BandType.LOW, 4680, 4690))
                .bandType(BandType.LOW)
                .lowerBound(4680d)
                .upperBound(4690d)
                .marketVisitCount(5)
                .build();

        MarketPriceBand hb3 = MarketPriceBand
                .builder()
                .bandKey(new BandKey(BandType.LOW, 4690, 4700))
                .bandType(BandType.LOW)
                .lowerBound(4690d)
                .upperBound(4700d)
                .marketVisitCount(23)
                .build();

        MarketPriceBand hb4 = MarketPriceBand
                .builder()
                .bandKey(new BandKey(BandType.LOW, 4700, 4710))
                .bandType(BandType.LOW)
                .lowerBound(4700d)
                .upperBound(4710d)
                .marketVisitCount(20)
                .build();

        MarketPriceBand hb5 = MarketPriceBand
                .builder()
                .bandKey(new BandKey(BandType.LOW, 4710, 4720))
                .bandType(BandType.LOW)
                .lowerBound(4710d)
                .upperBound(4720d)
                .marketVisitCount(7)
                .build();

        MarketPriceBand hb6 = MarketPriceBand
                .builder()
                .bandKey(new BandKey(BandType.LOW, 4720, 4730))
                .bandType(BandType.LOW)
                .lowerBound(4720d)
                .upperBound(4730d)
                .marketVisitCount(7)
                .build();

        MarketPriceBand hb7 = MarketPriceBand
                .builder()
                .bandKey(new BandKey(BandType.LOW, 4730, 4740))
                .bandType(BandType.LOW)
                .lowerBound(4730d)
                .upperBound(4740d)
                .marketVisitCount(20)
                .build();

        MarketPriceBand hb8 = MarketPriceBand
                .builder()
                .bandKey(new BandKey(BandType.LOW, 4740, 4750))
                .bandType(BandType.LOW)
                .lowerBound(4740d)
                .upperBound(4750d)
                .marketVisitCount(7)
                .build();

        MarketPriceBand hb9 = MarketPriceBand
                .builder()
                .bandKey(new BandKey(BandType.LOW, 4750, 4760))
                .bandType(BandType.LOW)
                .lowerBound(4750d)
                .upperBound(4760d)
                .marketVisitCount(2)
                .build();
        MarketPriceBand hb10 = MarketPriceBand
                .builder()
                .bandKey(new BandKey(BandType.LOW, 4760, 4770))
                .bandType(BandType.LOW)
                .lowerBound(4760d)
                .upperBound(4770d)
                .marketVisitCount(0)
                .build();
        MarketPriceBand hb11 = MarketPriceBand
                .builder()
                .bandKey(new BandKey(BandType.LOW, 4770, 4780))
                .bandType(BandType.LOW)
                .lowerBound(4770d)
                .upperBound(4780d)
                .marketVisitCount(2)
                .build();
        MarketPriceBand hb12 = MarketPriceBand
                .builder()
                .bandKey(new BandKey(BandType.LOW, 4780, 4790))
                .bandType(BandType.LOW)
                .lowerBound(4780d)
                .upperBound(4790d)
                .marketVisitCount(0)
                .build();
        MarketPriceBand hb13 = MarketPriceBand
                .builder()
                .bandKey(new BandKey(BandType.LOW, 4790, 4800))
                .bandType(BandType.LOW)
                .lowerBound(4790d)
                .upperBound(4800d)
                .marketVisitCount(0)
                .build();
        MarketPriceBand hb14 = MarketPriceBand
                .builder()
                .bandKey(new BandKey(BandType.LOW, 4800, 4810))
                .bandType(BandType.LOW)
                .lowerBound(4800d)
                .upperBound(4810d)
                .marketVisitCount(0)
                .build();
        return new TreeSet<>(List.of(hb0, hb1, hb2, hb3, hb4, hb5, hb6, hb7, hb8, hb9, hb10, hb11, hb12, hb13, hb14));
    }

    private NavigableSet<MarketPriceBand> buildExpectedInitialLowerBands()
    {
        MarketPriceBand lb0 = MarketPriceBand
                .builder()
                .bandKey(new BandKey(BandType.LOW, 4680, 4690))
                .bandType(BandType.LOW)
                .lowerBound(4680d)
                .upperBound(4690d)
                .marketVisitCount(5)
                .build();
        MarketPriceBand lb1 = MarketPriceBand
                .builder()
                .bandKey(new BandKey(BandType.LOW, 4690, 4700))
                .bandType(BandType.LOW)
                .lowerBound(4690d)
                .upperBound(4700d)
                .marketVisitCount(23)
                .build();
        MarketPriceBand lb2 = MarketPriceBand
                .builder()
                .bandKey(new BandKey(BandType.LOW, 4700, 4710))
                .bandType(BandType.LOW)
                .lowerBound(4700d)
                .upperBound(4710d)
                .marketVisitCount(20)
                .build();
        MarketPriceBand lb3 = MarketPriceBand
                .builder()
                .bandKey(new BandKey(BandType.LOW, 4710, 4720))
                .bandType(BandType.LOW)
                .lowerBound(4710d)
                .upperBound(4720d)
                .marketVisitCount(7)
                .build();
        MarketPriceBand lb4 = MarketPriceBand
                .builder()
                .bandKey(new BandKey(BandType.LOW, 4720, 4730))
                .bandType(BandType.LOW)
                .lowerBound(4720d)
                .upperBound(4730d)
                .marketVisitCount(7)
                .build();

        MarketPriceBand lb5 = MarketPriceBand
                .builder()
                .bandKey(new BandKey(BandType.LOW, 4730, 4740))
                .bandType(BandType.LOW)
                .lowerBound(4730d)
                .upperBound(4740d)
                .marketVisitCount(20)
                .build();

        MarketPriceBand lb6 = MarketPriceBand
                .builder()
                .bandKey(new BandKey(BandType.LOW, 4740, 4750))
                .bandType(BandType.LOW)
                .lowerBound(4740d)
                .upperBound(4750d)
                .marketVisitCount(7)
                .build();

        MarketPriceBand lb7 = MarketPriceBand
                .builder()
                .bandKey(new BandKey(BandType.LOW, 4750, 4760))
                .bandType(BandType.LOW)
                .lowerBound(4750d)
                .upperBound(4760d)
                .marketVisitCount(2)
                .build();

        MarketPriceBand lb8 = MarketPriceBand
                .builder()
                .bandKey(new BandKey(BandType.LOW, 4760, 4770))
                .bandType(BandType.LOW)
                .lowerBound(4760d)
                .upperBound(4770d)
                .marketVisitCount(0)
                .build();

        return new TreeSet<>(List.of(lb0, lb1, lb2, lb3, lb4, lb5, lb6, lb7, lb8));
    }

    private NavigableSet<MarketPriceBand> buildExpectedInitialUpperBands()
    {
        MarketPriceBand hb0 = MarketPriceBand
                .builder()
                .bandKey(new BandKey(BandType.HIGH, 4680, 4690))
                .bandType(BandType.HIGH)
                .lowerBound(4680d)
                .upperBound(4690d)
                .marketVisitCount(0)
                .build();

        MarketPriceBand hb1 = MarketPriceBand
                .builder()
                .bandKey(new BandKey(BandType.HIGH, 4690, 4700))
                .bandType(BandType.HIGH)
                .lowerBound(4690d)
                .upperBound(4700d)
                .marketVisitCount(11)
                .build();

        MarketPriceBand hb2 = MarketPriceBand
                .builder()
                .bandKey(new BandKey(BandType.HIGH, 4700, 4710))
                .bandType(BandType.HIGH)
                .lowerBound(4700d)
                .upperBound(4710d)
                .marketVisitCount(16)
                .build();

        MarketPriceBand hb3 = MarketPriceBand
                .builder()
                .bandKey(new BandKey(BandType.HIGH, 4710, 4720))
                .bandType(BandType.HIGH)
                .lowerBound(4710d)
                .upperBound(4720d)
                .marketVisitCount(22)
                .build();

        MarketPriceBand hb4 = MarketPriceBand
                .builder()
                .bandKey(new BandKey(BandType.HIGH, 4720, 4730))
                .bandType(BandType.HIGH)
                .lowerBound(4720d)
                .upperBound(4730d)
                .marketVisitCount(5)
                .build();

        MarketPriceBand hb5 = MarketPriceBand
                .builder()
                .bandKey(new BandKey(BandType.HIGH, 4730, 4740))
                .bandType(BandType.HIGH)
                .lowerBound(4730d)
                .upperBound(4740d)
                .marketVisitCount(9)
                .build();

        MarketPriceBand hb6 = MarketPriceBand
                .builder()
                .bandKey(new BandKey(BandType.HIGH, 4740, 4750))
                .bandType(BandType.HIGH)
                .lowerBound(4740d)
                .upperBound(4750d)
                .marketVisitCount(13)
                .build();

        MarketPriceBand hb7 = MarketPriceBand
                .builder()
                .bandKey(new BandKey(BandType.HIGH, 4750, 4760))
                .bandType(BandType.HIGH)
                .lowerBound(4750d)
                .upperBound(4760d)
                .marketVisitCount(12)
                .build();

        MarketPriceBand hb8 = MarketPriceBand
                .builder()
                .bandKey(new BandKey(BandType.HIGH, 4760, 4770))
                .bandType(BandType.HIGH)
                .lowerBound(4760d)
                .upperBound(4770d)
                .marketVisitCount(3)
                .build();
        return new TreeSet<>(List.of(hb0, hb1, hb2, hb3, hb4, hb5, hb6, hb7, hb8));
    }


    private NavigableSet<MarketPriceBand> buildExpectedModifiedUpperBands()
    {
        MarketPriceBand hb0 = MarketPriceBand
                .builder()
                .bandKey(new BandKey(BandType.HIGH, 4660, 4670))
                .bandType(BandType.HIGH)
                .lowerBound(4660d)
                .upperBound(4670d)
                .marketVisitCount(0)
                .build();

        MarketPriceBand hb1 = MarketPriceBand
                .builder()
                .bandKey(new BandKey(BandType.HIGH, 4670, 4680))
                .bandType(BandType.HIGH)
                .lowerBound(4670d)
                .upperBound(4680d)
                .marketVisitCount(0)
                .build();

        MarketPriceBand hb2 = MarketPriceBand
                .builder()
                .bandKey(new BandKey(BandType.HIGH, 4680, 4690))
                .bandType(BandType.HIGH)
                .lowerBound(4680d)
                .upperBound(4690d)
                .marketVisitCount(1)
                .build();

        MarketPriceBand hb3 = MarketPriceBand
                .builder()
                .bandKey(new BandKey(BandType.HIGH, 4690, 4700))
                .bandType(BandType.HIGH)
                .lowerBound(4690d)
                .upperBound(4700d)
                .marketVisitCount(11)
                .build();

        MarketPriceBand hb4 = MarketPriceBand
                .builder()
                .bandKey(new BandKey(BandType.HIGH, 4700, 4710))
                .bandType(BandType.HIGH)
                .lowerBound(4700d)
                .upperBound(4710d)
                .marketVisitCount(16)
                .build();

        MarketPriceBand hb5 = MarketPriceBand
                .builder()
                .bandKey(new BandKey(BandType.HIGH, 4710, 4720))
                .bandType(BandType.HIGH)
                .lowerBound(4710d)
                .upperBound(4720d)
                .marketVisitCount(22)
                .build();

        MarketPriceBand hb6 = MarketPriceBand
                .builder()
                .bandKey(new BandKey(BandType.HIGH, 4720, 4730))
                .bandType(BandType.HIGH)
                .lowerBound(4720d)
                .upperBound(4730d)
                .marketVisitCount(5)
                .build();

        MarketPriceBand hb7 = MarketPriceBand
                .builder()
                .bandKey(new BandKey(BandType.HIGH, 4730, 4740))
                .bandType(BandType.HIGH)
                .lowerBound(4730d)
                .upperBound(4740d)
                .marketVisitCount(9)
                .build();

        MarketPriceBand hb8 = MarketPriceBand
                .builder()
                .bandKey(new BandKey(BandType.HIGH, 4740, 4750))
                .bandType(BandType.HIGH)
                .lowerBound(4740d)
                .upperBound(4750d)
                .marketVisitCount(13)
                .build();

        MarketPriceBand hb9 = MarketPriceBand
                .builder()
                .bandKey(new BandKey(BandType.HIGH, 4750, 4760))
                .bandType(BandType.HIGH)
                .lowerBound(4750d)
                .upperBound(4760d)
                .marketVisitCount(12)
                .build();

        MarketPriceBand hb10 = MarketPriceBand
                .builder()
                .bandKey(new BandKey(BandType.HIGH, 4760, 4770))
                .bandType(BandType.HIGH)
                .lowerBound(4760d)
                .upperBound(4770d)
                .marketVisitCount(3)
                .build();

        MarketPriceBand hb11 = MarketPriceBand
                .builder()
                .bandKey(new BandKey(BandType.HIGH, 4770, 4780))
                .bandType(BandType.HIGH)
                .lowerBound(4770d)
                .upperBound(4780d)
                .marketVisitCount(0)
                .build();

        MarketPriceBand hb12 = MarketPriceBand
                .builder()
                .bandKey(new BandKey(BandType.HIGH, 4780, 4790))
                .bandType(BandType.HIGH)
                .lowerBound(4780d)
                .upperBound(4790d)
                .marketVisitCount(1)
                .build();

        MarketPriceBand hb13 = MarketPriceBand
                .builder()
                .bandKey(new BandKey(BandType.HIGH, 4790, 4800))
                .bandType(BandType.HIGH)
                .lowerBound(4790d)
                .upperBound(4800d)
                .marketVisitCount(0)
                .build();

        MarketPriceBand hb14 = MarketPriceBand
                .builder()
                .bandKey(new BandKey(BandType.HIGH, 4800, 4810))
                .bandType(BandType.HIGH)
                .lowerBound(4800d)
                .upperBound(4810d)
                .marketVisitCount(1)
                .build();
        return new TreeSet<>(List.of(hb0, hb1, hb2, hb3, hb4, hb5, hb6, hb7, hb8, hb9, hb10, hb11, hb12, hb13, hb14));
    }


}