package com.hkcapital.portflio.indicators;

import com.hkcapital.portflio.market.indicators.TimeFramesUnit;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


class UnitTest
{
    @Test
    void verifyConstants() //
    {
        TimeFramesUnit[] expected = new TimeFramesUnit[]{TimeFramesUnit.MINUTE, TimeFramesUnit.HOUR, TimeFramesUnit.DAY, TimeFramesUnit.WEEK};
        TimeFramesUnit[] actual = TimeFramesUnit.values();
        Assertions.assertArrayEquals(actual, expected);
    }
}