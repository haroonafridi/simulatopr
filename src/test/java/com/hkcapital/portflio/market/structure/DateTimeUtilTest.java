package com.hkcapital.portflio.market.structure;

import com.hkcapital.portflio.market.indicators.TimeFramesUnit;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.*;
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
    public void shouldCreateInstant()
    {
        ZoneId zone = ZoneId.systemDefault();
        Instant fromDate = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0).atZone(zone).toInstant();
        Instant toDate = LocalDateTime.now().withHour(23).withMinute(59).withSecond(59).withNano(0).atZone(zone).toInstant();
        System.out.println("From date => " + fromDate);
        System.out.println("To date => " + toDate);
    }

    @Test
    public void shouldReturnLocalDateTime()
    {
        LocalDateTime d1 = DateTimeUtil.localDateTimeFrom("2026-05-31 13:53:37.754973");
        LocalDateTime d2 = LocalDateTime.parse("2026-05-31 13:53:37.754973", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS"));
        Assertions.assertEquals(d1, d2);
    }

    @Test
    public void shoudlRetrunYearMonthDay() //
    {
        final Instant someDate = Instant.parse("2007-12-03T10:15:30.00Z");
        final String date = DateTimeUtil.toYearMonthDay(someDate);
        Assertions.assertEquals("2007_12_03", date);
    }

    @Test
    public void shouldTextToLocalDateTime()
    {
        LocalDateTime fromDate = DateTimeUtil.asDayStart("2007-12-03");
        LocalDateTime toDate =  DateTimeUtil.asDayEnd("2007-12-03");
        Assertions.assertEquals("2007-12-03T00:00", fromDate.toString());
        Assertions.assertEquals("2007-12-03T23:59:59.999999999", toDate.toString());
    }

    @Test
    public void shouldConvertTextToInstant()
    {
        Instant fromDate = DateTimeUtil.asDayStart("2007-12-03", ZoneOffset.UTC);
        Instant toDate =  DateTimeUtil.asDayEnd("2007-12-03", ZoneOffset.UTC);
        Assertions.assertEquals("2007-12-03T00:00:00Z", fromDate.toString());
        Assertions.assertEquals("2007-12-03T23:59:59.999999999Z", toDate.toString());
    }


    @Test
    public void shouldConvertInstantToLocalDateTime()
    {
        Instant fromDate = DateTimeUtil.asDayStart("2007-12-03", ZoneOffset.UTC);
        LocalDateTime fromLocalDateTime = DateTimeUtil.asLocalDateTime(fromDate,ZoneOffset.UTC);
        Instant toDate =  DateTimeUtil.asDayEnd("2007-12-03", ZoneOffset.UTC);
        LocalDateTime toLocalDateTime = DateTimeUtil.asLocalDateTime(toDate,ZoneOffset.UTC);
        Assertions.assertEquals("2007-12-03T00:00", fromLocalDateTime.toString());
        Assertions.assertEquals("2007-12-03T23:59:59.999999999", toLocalDateTime.toString());
    }
}