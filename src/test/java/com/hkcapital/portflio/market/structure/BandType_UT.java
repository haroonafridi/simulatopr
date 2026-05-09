package com.hkcapital.portflio.market.structure;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

class BandType_UT
{
    @Test
    public void shouldReturnHighOnlyAndLow()
    {
        BandType[] bandTypes = BandType.values();
        Assertions.assertEquals(2, bandTypes.length);
        BandType high = Arrays.stream(bandTypes).filter(b -> b.equals(BandType.HIGH)).findAny().get();
        BandType low = Arrays.stream(bandTypes).filter(b -> b.equals(BandType.LOW)).findAny().get();
        Assertions.assertEquals(BandType.HIGH, high);
        Assertions.assertEquals(BandType.LOW, low);
    }
}