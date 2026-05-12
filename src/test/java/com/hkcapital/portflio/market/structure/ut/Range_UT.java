package com.hkcapital.portflio.market.structure.ut;

import com.hkcapital.portflio.market.structure.Range;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class Range_UT
{

    @Test
    public void shouldReturnRange()
    {
        final double low = 4700d;
        final double high = 4700d;
        Range range = Range.builder()
                .low(low) //
                .high(high) //
                .build();

        Assertions.assertAll("Verify Range ", () ->
        {
            Assertions.assertEquals(low, range.getLow());
            Assertions.assertEquals(high, range.getHigh());
        });
    }

}