package com.hkcapital.portflio.market.structure.ut;

import com.hkcapital.portflio.market.structure.BandKey;
import com.hkcapital.portflio.market.structure.BandType;
import com.hkcapital.portflio.market.structure.MarketPriceBand;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

class MarketPriceBand_UT
{

    @Test
    public void shouldCreateHighBand()
    {
        MarketPriceBand highBand4680_4689 = MarketPriceBand
                .builder().bandType(BandType.HIGH)
                .bandKey(new BandKey(BandType.HIGH, 4680, 4689))
                .lowerBound(4680d)
                .upperBound(4689d)
                .marketVisitCount(1)
                .initialVisitedTime(LocalDateTime.now())
                .build();
        Assertions.assertAll(() ->
        {
            Assertions.assertEquals(BandType.HIGH, highBand4680_4689.getBandType());
            Assertions.assertEquals("high_4680_4689", highBand4680_4689.getBandKey().toString());
            Assertions.assertEquals(4680d, highBand4680_4689.getLowerBound());
            Assertions.assertEquals(4689d, highBand4680_4689.getUpperBound());
            Assertions.assertEquals(1, highBand4680_4689.getMarketVisitCount());
        });
    }

    @Test
    public void shouldCreateLowerBand()
    {
        MarketPriceBand lowBand4680_4689 = MarketPriceBand
                .builder().bandType(BandType.LOW)
                .bandKey(new BandKey(BandType.LOW, 4680, 4689))
                .lowerBound(4680d)
                .upperBound(4689d)
                .initialVisitedTime(LocalDateTime.now())
                .marketVisitCount(6)
                .build();
        Assertions.assertAll(() ->
        {
            Assertions.assertEquals(BandType.LOW, lowBand4680_4689.getBandType());
            Assertions.assertEquals("low_4680_4689", lowBand4680_4689.getBandKey().toString());
            Assertions.assertEquals(4680d, lowBand4680_4689.getLowerBound());
            Assertions.assertEquals(4689d, lowBand4680_4689.getUpperBound());
            Assertions.assertEquals(6, lowBand4680_4689.getMarketVisitCount());
        });
    }


    @Test
    public void shouldCreateAsiaSessionUpperBands()
    {
        final MarketPriceBand highBand4680_4689 = MarketPriceBand
                .builder().bandType(BandType.HIGH)
                .bandKey(new BandKey(BandType.HIGH, 4680, 4689))
                .lowerBound(4680d)
                .upperBound(4689d)
                .initialVisitedTime(LocalDateTime.now())
                .marketVisitCount(1)
                .build();

        Assertions.assertAll("verify upper band of [high_4680_4689]", () ->
        {
            Assertions.assertEquals(BandType.HIGH, highBand4680_4689.getBandType());
            Assertions.assertEquals("high_4680_4689", highBand4680_4689.getBandKey().toString());
            Assertions.assertEquals(4680d, highBand4680_4689.getLowerBound());
            Assertions.assertEquals(4689d, highBand4680_4689.getUpperBound());
            Assertions.assertEquals(1, highBand4680_4689.getMarketVisitCount());
        });

        final MarketPriceBand highBand4690_4699 = MarketPriceBand
                .builder().bandType(BandType.HIGH)
                .bandKey(new BandKey(BandType.HIGH, 4690, 4699))
                .lowerBound(4690d)
                .upperBound(4699d)
                .initialVisitedTime(LocalDateTime.now())
                .marketVisitCount(6)
                .build();

        Assertions.assertAll("verify upper band of [hig_4690_4699]", () ->
        {
            Assertions.assertEquals(BandType.HIGH, highBand4690_4699.getBandType());
            Assertions.assertEquals("high_4690_4699", highBand4690_4699.getBandKey().toString());
            Assertions.assertEquals(4690d, highBand4690_4699.getLowerBound());
            Assertions.assertEquals(4699d, highBand4690_4699.getUpperBound());
            Assertions.assertEquals(6, highBand4690_4699.getMarketVisitCount());
        });

        final MarketPriceBand highBand4700_4709 = MarketPriceBand
                .builder().bandType(BandType.HIGH)
                .bandKey(new BandKey(BandType.HIGH, 4700, 4709))
                .lowerBound(4700d)
                .upperBound(4709d)
                .initialVisitedTime(LocalDateTime.now())
                .marketVisitCount(5)
                .build();

        Assertions.assertAll("verify upper band of [high_4700_4709]", () ->
        {
            Assertions.assertEquals(BandType.HIGH, highBand4700_4709.getBandType());
            Assertions.assertEquals("high_4700_4709", highBand4700_4709.getBandKey().toString());
            Assertions.assertEquals(4700d, highBand4700_4709.getLowerBound());
            Assertions.assertEquals(4709d, highBand4700_4709.getUpperBound());
            Assertions.assertEquals(5, highBand4700_4709.getMarketVisitCount());
        });

        final MarketPriceBand highBand4710_4719 = MarketPriceBand
                .builder().bandType(BandType.HIGH)
                .bandKey(new BandKey(BandType.HIGH, 4710, 4719))
                .lowerBound(4710d)
                .upperBound(4719d)
                .initialVisitedTime(LocalDateTime.now())
                .marketVisitCount(8)
                .build();


        Assertions.assertAll("verify upper band of [high_4710_4719]", () ->
        {
            Assertions.assertEquals(BandType.HIGH, highBand4710_4719.getBandType());
            Assertions.assertEquals("high_4710_4719", highBand4710_4719.getBandKey().toString());
            Assertions.assertEquals(4710d, highBand4710_4719.getLowerBound());
            Assertions.assertEquals(4719d, highBand4710_4719.getUpperBound());
            Assertions.assertEquals(8, highBand4710_4719.getMarketVisitCount());
        });

        final MarketPriceBand highBand4720_4729 = MarketPriceBand
                .builder().bandType(BandType.HIGH)
                .bandKey(new BandKey(BandType.HIGH, 4720, 4729))
                .lowerBound(4720d)
                .upperBound(4729d)
                .initialVisitedTime(LocalDateTime.now())
                .marketVisitCount(9)
                .build();

        Assertions.assertAll("verify upper band of [high_4720_4729]", () ->
        {
            Assertions.assertEquals(BandType.HIGH, highBand4720_4729.getBandType());
            Assertions.assertEquals("high_4720_4729", highBand4720_4729.getBandKey().toString());
            Assertions.assertEquals(4720d, highBand4720_4729.getLowerBound());
            Assertions.assertEquals(4729d, highBand4720_4729.getUpperBound());
            Assertions.assertEquals(9, highBand4720_4729.getMarketVisitCount());
        });

        final MarketPriceBand highBand4730_4739 = MarketPriceBand
                .builder().bandType(BandType.HIGH)
                .bandKey(new BandKey(BandType.HIGH, 4730, 4739))
                .lowerBound(4730d)
                .upperBound(4739d)
                .initialVisitedTime(LocalDateTime.now())
                .marketVisitCount(6)
                .build();

        Assertions.assertAll("verify upper band of [high_4730_4739]", () ->
        {
            Assertions.assertEquals(BandType.HIGH, highBand4730_4739.getBandType());
            Assertions.assertEquals("high_4730_4739", highBand4730_4739.getBandKey().toString());
            Assertions.assertEquals(4730d, highBand4730_4739.getLowerBound());
            Assertions.assertEquals(4739d, highBand4730_4739.getUpperBound());
            Assertions.assertEquals(6, highBand4730_4739.getMarketVisitCount());
        });
    }

    @Test
    public void shouldCreateAsiaSessionLowerBands()
    {
        final MarketPriceBand highBand4680_4689 = MarketPriceBand
                .builder().bandType(BandType.LOW)
                .bandKey(new BandKey(BandType.LOW, 4680, 4689))
                .lowerBound(4680d)
                .upperBound(4689d)
                .initialVisitedTime(LocalDateTime.now())
                .marketVisitCount(6)
                .build();

        Assertions.assertAll("verify lower band of [low_4680_4689]", () ->
        {
            Assertions.assertEquals(BandType.LOW, highBand4680_4689.getBandType());
            Assertions.assertEquals("low_4680_4689", highBand4680_4689.getBandKey().toString());
            Assertions.assertEquals(4680d, highBand4680_4689.getLowerBound());
            Assertions.assertEquals(4689d, highBand4680_4689.getUpperBound());
            Assertions.assertEquals(6, highBand4680_4689.getMarketVisitCount());
        });

        final MarketPriceBand highBand4690_4699 = MarketPriceBand
                .builder().bandType(BandType.LOW)
                .bandKey(new BandKey(BandType.LOW, 4690, 4699))
                .lowerBound(4690d)
                .upperBound(4699d)
                .initialVisitedTime(LocalDateTime.now())
                .marketVisitCount(6)
                .build();

        Assertions.assertAll("verify lower band of [low_4690_4699]", () ->
        {
            Assertions.assertEquals(BandType.LOW, highBand4690_4699.getBandType());
            Assertions.assertEquals("low_4690_4699", highBand4690_4699.getBandKey().toString());
            Assertions.assertEquals(4690d, highBand4690_4699.getLowerBound());
            Assertions.assertEquals(4699d, highBand4690_4699.getUpperBound());
            Assertions.assertEquals(6, highBand4690_4699.getMarketVisitCount());
        });

        final MarketPriceBand highBand4700_4709 = MarketPriceBand
                .builder().bandType(BandType.LOW)
                .bandKey(new BandKey(BandType.LOW, 4700, 4709))
                .lowerBound(4700d)
                .upperBound(4709d)
                .initialVisitedTime(LocalDateTime.now())
                .marketVisitCount(6)
                .build();

        Assertions.assertAll("verify lower band of low_[4700_4709]", () ->
        {
            Assertions.assertEquals(BandType.LOW, highBand4700_4709.getBandType());
            Assertions.assertEquals("low_4700_4709", highBand4700_4709.getBandKey().toString());
            Assertions.assertEquals(4700d, highBand4700_4709.getLowerBound());
            Assertions.assertEquals(4709d, highBand4700_4709.getUpperBound());
            Assertions.assertEquals(6, highBand4700_4709.getMarketVisitCount());
        });

        final MarketPriceBand highBand4710_4719 = MarketPriceBand
                .builder().bandType(BandType.LOW)
                .bandKey(new BandKey(BandType.LOW, 4710, 4719))
                .lowerBound(4710d)
                .upperBound(4719d)
                .initialVisitedTime(LocalDateTime.now())
                .marketVisitCount(9)
                .build();


        Assertions.assertAll("verify lower band of [low_4710_4719]", () ->
        {
            Assertions.assertEquals(BandType.LOW, highBand4710_4719.getBandType());
            Assertions.assertEquals("low_4710_4719", highBand4710_4719.getBandKey().toString());
            Assertions.assertEquals(4710d, highBand4710_4719.getLowerBound());
            Assertions.assertEquals(4719d, highBand4710_4719.getUpperBound());
            Assertions.assertEquals(9, highBand4710_4719.getMarketVisitCount());
        });

        final MarketPriceBand highBand4720_4729 = MarketPriceBand
                .builder().bandType(BandType.LOW)
                .bandKey(new BandKey(BandType.LOW, 4720, 4729))
                .lowerBound(4720d)
                .upperBound(4729d)
                .initialVisitedTime(LocalDateTime.now())
                .marketVisitCount(5)
                .build();

        Assertions.assertAll("verify lower band of [low_4720_4729]", () ->
        {
            Assertions.assertEquals(BandType.LOW, highBand4720_4729.getBandType());
            Assertions.assertEquals("low_4720_4729", highBand4720_4729.getBandKey().toString());
            Assertions.assertEquals(4720d, highBand4720_4729.getLowerBound());
            Assertions.assertEquals(4729d, highBand4720_4729.getUpperBound());
            Assertions.assertEquals(5, highBand4720_4729.getMarketVisitCount());
        });

        final MarketPriceBand highBand4730_4739 = MarketPriceBand
                .builder().bandType(BandType.LOW)
                .bandKey(new BandKey(BandType.LOW, 4730, 4739))
                .lowerBound(4730d)
                .upperBound(4739d)
                .initialVisitedTime(LocalDateTime.now())
                .marketVisitCount(0)
                .build();

        Assertions.assertAll("verify lower band of [low_4730_4739]", () ->
        {
            Assertions.assertEquals(BandType.LOW, highBand4730_4739.getBandType());
            Assertions.assertEquals("low_4730_4739", highBand4730_4739.getBandKey().toString());
            Assertions.assertEquals(4730d, highBand4730_4739.getLowerBound());
            Assertions.assertEquals(4739d, highBand4730_4739.getUpperBound());
            Assertions.assertEquals(0, highBand4730_4739.getMarketVisitCount());
        });
    }

}