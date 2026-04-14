package com.hkcapital.portoflio.indicators;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.ZoneId;

import static com.hkcapital.portoflio.indicators.ChronoFieldUtil.*;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ChronoFieldUtilTest
{
    private static Instant SOME_DATE = Instant.parse("2026-03-16T23:11:49.1874723Z");
    private static final ZoneId UTC = ZoneId.of("UTC");

    @Test
    @DisplayName("Should correctly extract second, minute, hour from instant")
    public void shouldExtractSecondMinuteHourDay()
    {
        assertAll(() -> assertEquals(11, minuteOfHour(SOME_DATE, ZoneId.systemDefault())),
                () -> assertEquals(49, secondOfMinute(SOME_DATE, ZoneId.systemDefault())),
                () -> assertEquals(0, hourOfDay(SOME_DATE, ZoneId.systemDefault())),
                () -> assertEquals(2, dayOfWeek(SOME_DATE, ZoneId.systemDefault())));
    }

    @Test
    @DisplayName("Should handle different timezone correct")
    public void shouldReturnsSecond()
    {
        assertAll(() -> assertEquals(11, minuteOfHour(SOME_DATE, UTC)),
                () -> assertEquals(49, secondOfMinute(SOME_DATE, UTC)),
                () -> assertEquals(23, hourOfDay(SOME_DATE, UTC)),
                () -> assertEquals(1, dayOfWeek(SOME_DATE, UTC)));
    }


}