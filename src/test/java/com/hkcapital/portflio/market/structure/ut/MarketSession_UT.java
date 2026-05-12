package com.hkcapital.portflio.market.structure.ut;

import com.hkcapital.portflio.market.structure.MarketSession;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Instant;

class MarketSession_UT
{

    @Test
    public void shouldCreateTokyoOpenSession()
    {

        final Instant start = Instant.parse("2026-05-07T02:00:00.00Z");
        final Instant end = Instant.parse("2026-05-07T08:59:59.00Z");
        final String name = "ASIA/TOKYO";
        final MarketSession marketSession = MarketSession.builder()
                .name(name)
                .start(start)
                .end(end)
                .build();
        Assertions.assertAll("verify ASIA/TOKYO Session", () ->
        {
            Assertions.assertEquals(name, marketSession.getName());
            Assertions.assertEquals(start, marketSession.getStart());
            Assertions.assertEquals(end, marketSession.getEnd());
        });
    }

}