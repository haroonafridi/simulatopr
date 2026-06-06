package com.hkcapital.portflio.market.structure;

import com.hkcapital.portflio.market.indicators.TimeFramesUnit;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

class DateTimeUtilTest
{


    @Test
    public void shouldReturnDiffInSeconds()
    {
        Instant d1 = LocalDateTime.of(2026, 05, 31, 12, 25, 15)
                .toInstant(ZoneOffset.UTC);
        Instant d2 = LocalDateTime.of(2026, 05, 31, 12, 39, 0)
                .toInstant(ZoneOffset.UTC);
        long diff = DateTimeUtil.minus(d2, d1, TimeFramesUnit.SECOND);
        Assertions.assertEquals(825, diff);
    }

    @Test
    public void shouldReturnDiffInMinutes()
    {
        Instant d1 = LocalDateTime.of(2026, 05, 31, 12, 25, 55) //
                .toInstant(ZoneOffset.UTC);
        Instant d2 = LocalDateTime.of(2026, 05, 31, 12, 39, 0) //
                .toInstant(ZoneOffset.UTC);
        long diff = DateTimeUtil.minus(d2, d1, TimeFramesUnit.MINUTE);
        Assertions.assertEquals(13, diff);
    }

    @Test
    public void shouldReturnDiffInHours()
    {
        Instant d1 = LocalDateTime.of(2026, 05, 31, 12, 25, 0) //
                .toInstant(ZoneOffset.UTC);
        Instant d2 = LocalDateTime.of(2026, 05, 31, 14, 39, 0) //
                .toInstant(ZoneOffset.UTC);
        long diff = DateTimeUtil.minus(d2, d1, TimeFramesUnit.HOUR);
        Assertions.assertEquals(2, diff);
    }

    @Test
    public void shouldReturnLocalDateTime()
    {
        LocalDateTime d1 = DateTimeUtil.localDateTimeFrom("2026-05-31 13:53:37.754973");
        LocalDateTime d2 = LocalDateTime.parse("2026-05-31 13:53:37.754973", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS"));
        Assertions.assertEquals(d1, d2);
    }
}