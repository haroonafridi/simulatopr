package com.hkcapital.portoflio.indicators;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

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

    @Test
    public void shouldReturnSameMinute()
    {
        Instant d1 = Instant.parse("2026-03-16T23:11:49.1874723Z");
        Instant d2 = Instant.parse("2026-03-16T23:11:50.1874723Z");
        Assertions.assertEquals(toBucket(d1, Unit.MINUTE, 1), toBucket(d2, Unit.MINUTE, 1));
    }


    @Test
    public void shouldReturnSame5Minute()
    {
        Instant d1 = Instant.parse("2026-03-16T23:11:49.1874723Z");
        Instant d2 = Instant.parse("2026-03-16T23:13:50.1874723Z");
        Assertions.assertEquals(toBucket(d1, Unit.MINUTE, 5), toBucket(d2, Unit.MINUTE, 5));
    }

    @Test
    public void shouldReturnSame10Minute()
    {
        Instant d1 = Instant.parse("2026-03-16T23:11:49.1874723Z");
        Instant d2 = Instant.parse("2026-03-16T23:19:50.1874723Z");
        Assertions.assertEquals(toBucket(d1, Unit.MINUTE, 10), toBucket(d2, Unit.MINUTE, 10));
    }

    @Test
    public void shouldReturnSame15Minute()
    {
        Instant d1 = Instant.parse("2026-03-16T23:11:49.1874723Z");
        Instant d2 = Instant.parse("2026-03-16T23:14:59.1874723Z");
        Assertions.assertEquals(toBucket(d1, Unit.MINUTE, 15), toBucket(d2, Unit.MINUTE, 15));
    }


    @Test
    public void shouldReturnSame30Minute()
    {
        Instant d1 = Instant.parse("2026-03-16T23:00:49.1874723Z");
        Instant d2 = Instant.parse("2026-03-16T23:29:59.1874723Z");
        Assertions.assertEquals(toBucket(d1, Unit.MINUTE, 30), toBucket(d2, Unit.MINUTE, 30));
    }


    @Test
    public void shouldReturnSame1Hour()
    {
        Instant d1 = Instant.parse("2026-03-16T23:00:49.1874723Z");
        Instant d2 = Instant.parse("2026-03-16T23:29:59.1874723Z");
        Assertions.assertEquals(toBucket(d1, Unit.HOUR, 1), toBucket(d2, Unit.HOUR, 1));
    }

    @Test
    public void shouldReturnDiff1Hour()
    {
        Instant d1 = Instant.parse("2026-03-16T22:00:49.1874723Z");
        Instant d2 = Instant.parse("2026-03-16T23:29:59.1874723Z");
        Assertions.assertNotEquals(toBucket(d1, Unit.HOUR, 1), toBucket(d2, Unit.HOUR, 1));
    }


    @Test
    public void shouldCreateInstant(){
       Assertions.assertEquals("2026-04-21T00:00:38.395309Z", //
               ChronoFieldUtil.parse("2026-04-21 00:00:38.395309").toString());
    }


}