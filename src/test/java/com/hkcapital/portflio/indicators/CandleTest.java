package com.hkcapital.portflio.indicators;

import com.hkcapital.portflio.market.indicators.CandleDto;
import com.hkcapital.portflio.market.indicators.TimeFramesUnit;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Instant;

class CandleTest
{
    @Test
    void shouldCreateCandle()
    {
        CandleDto candle = new CandleDto(null, 5400.52, 5380.33, Instant.parse("2026-04-09T18:00:01.00Z"),
                5410.20, Instant.parse("2026-04-09T18:00:05.00Z"),
                5405.55, Instant.parse("2026-04-09T18:00:06.00Z"), TimeFramesUnit.MINUTE, 15);
        Assertions.assertEquals(candle.getInstrument(), "18");
        Assertions.assertEquals(candle.getOpen(), 5400.52);
        Assertions.assertEquals(candle.getLow(), 5380.33);
        Assertions.assertEquals(candle.getHigh(), 5410.20);
        Assertions.assertEquals(candle.getClose(), 5405.55);
        Assertions.assertEquals(candle.getTimeFramesUnit(), TimeFramesUnit.MINUTE);
        Assertions.assertEquals(candle.getInterval(), 15);
    }
}