package com.hkcapital.portoflio.indicators;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Instant;

class TickTest
{

    @Test
    public void testTickData()
    {
        Tick tick = Tick.builder()
                .instrument("18")
                .time(Instant.parse("2007-12-03T10:15:30.00Z"))
                .val(5400.53)
                .build();
        Assertions.assertEquals(tick.getInstrument(), "18");
        Assertions.assertEquals(tick.getTime(), Instant.parse("2007-12-03T10:15:30.00Z"));
        Assertions.assertEquals(tick.getVal(), 5400.53);
    }

}