package com.hkcapital.portoflio.indicators;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


class UnitTest
{
    @Test
    void verifyConstants() //
    {
        Unit[] expected = new Unit[]{Unit.MINUTE, Unit.HOUR, Unit.DAY, Unit.WEEK};
        Unit[] actual = Unit.values();
        Assertions.assertArrayEquals(actual, expected);
    }
}