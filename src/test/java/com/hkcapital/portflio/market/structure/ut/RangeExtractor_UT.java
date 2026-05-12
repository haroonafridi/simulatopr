package com.hkcapital.portflio.market.structure.ut;

import com.hkcapital.portflio.market.structure.Modus;
import com.hkcapital.portflio.market.structure.PreviousDayMarketRange;
import com.hkcapital.portflio.market.structure.Range;
import com.hkcapital.portflio.market.structure.RangeExtractor;
import com.hkcapital.portflio.model.Instrument;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Instant;

class RangeExtractor_UT
{

    @Test
    public void shouldNormalizeRange()
    {

        final String instName = "GOLD";
        final String instTicker = "XAU/USD";
        final String instDesc = "Gold Standard CFD";
        final double maxSlippage = 1.75;
        final boolean active = true;
        final Integer etoroInstId = 18;
        final Instant date = Instant.parse("2026-05-07T23:59:59.00Z");
        final double low = 4686.33;
        final double high = 4749.97;

        final double normalizedLow = 4680;
        final double normalizedHigh = 4749;

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

        Assertions.assertAll("should verify Previous day market range!", () ->
        {
            Assertions.assertEquals(instrument, dayRange.getInstrument());
            Assertions.assertEquals(date, dayRange.getDate());
            Assertions.assertEquals(low, dayRange.getLow());
            Assertions.assertEquals(high, dayRange.getHigh());
        });

        final Range range = RangeExtractor.of(dayRange, Modus.builder().mod(10)
                .subtract(9)
                .build());

        Assertions.assertAll("should Normalize Range ", () ->
        {
            Assertions.assertNotNull(range);
            Assertions.assertEquals(normalizedLow, range.getLow());
            Assertions.assertEquals(normalizedHigh, range.getHigh());
        });
    }
}