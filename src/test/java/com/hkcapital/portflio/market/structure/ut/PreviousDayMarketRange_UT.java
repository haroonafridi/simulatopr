package com.hkcapital.portflio.market.structure.ut;

import com.hkcapital.portflio.market.structure.PreviousDayMarketRange;
import com.hkcapital.portflio.model.Instrument;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Instant;

class PreviousDayMarketRange_UT
{
    @Test
    public void shouldCreateGoldPreviousDayRange()
    {
        final String instName = "GOLD";
        final String instTicker = "XAU/USD";
        final String instDesc = "Gold Standard CFD";
        final double maxSlippage = 1.75;
        final boolean active = true;
        final Integer etoroInstId = 18;
        final Instant date = Instant.parse("2026-05-07T23:59:59.00Z");
        final double low = 4681.33;
        final double high = 4748.97;

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

        Assertions.assertAll("should verify Previous day market range!", () ->
        {
            Assertions.assertEquals(instrument, range.getInstrument());
            Assertions.assertEquals(date, range.getDate());
            Assertions.assertEquals(low, range.getLow());
            Assertions.assertEquals(high, range.getHigh());
        });
    }

}