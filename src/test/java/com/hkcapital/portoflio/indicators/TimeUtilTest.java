package com.hkcapital.portoflio.indicators;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.ZoneId;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class TimeUtilTest
{
    private static Instant SOME_DATE = Instant.parse("2026-03-16T23:11:49.1874723Z");
    private static final ZoneId UTC = ZoneId.of("UTC");

    @Test
    @DisplayName("Should correctly extract minute and second from instant")
    public void shouldExtractMinuteAndSecond()
    {
        assertAll(() -> assertEquals(11, TimeUtil.minuteOfHour(SOME_DATE, ZoneId.systemDefault())),
                () -> assertEquals(49, TimeUtil.secondOfMinute(SOME_DATE, ZoneId.systemDefault())));
    }


    @Test
    @DisplayName("Should handle different timezone correct")
    public void shouldReturnsSecond()
    {
        assertAll(() -> assertEquals(11, TimeUtil.minuteOfHour(SOME_DATE, UTC)),
                () -> assertEquals(49, TimeUtil.secondOfMinute(SOME_DATE, UTC)));
    }


}