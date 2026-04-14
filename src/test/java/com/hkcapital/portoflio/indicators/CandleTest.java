package com.hkcapital.portoflio.indicators;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Instant;

class CandleTest
{
    @Test
    void shouldCreateCandle()
    {
        Candle candle = new Candle("18", 5400.52, 5380.33, 5410.20, 5405.55,
                Instant.parse("2026-04-09T18:00:01.00Z"), Unit.MINUTE, 15);
        Assertions.assertEquals(candle.getInstrument(), "18");
        Assertions.assertEquals(candle.getOpen(), 5400.52);
        Assertions.assertEquals(candle.getLow(), 5380.33);
        Assertions.assertEquals(candle.getHigh(), 5410.20);
        Assertions.assertEquals(candle.getClose(), 5405.55);
        Assertions.assertEquals(candle.getUnit(), Unit.MINUTE);
        Assertions.assertEquals(candle.getInterval(), 15);
    }
}