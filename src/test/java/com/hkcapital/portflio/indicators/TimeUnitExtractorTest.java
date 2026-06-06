package com.hkcapital.portflio.indicators;

import com.hkcapital.portflio.market.indicators.ChronoFieldUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoField;

class TimeUnitExtractorTest
{
    private final String date = "2026-06-01 19:48:59.384058";
    @Test
    public void testTemoralExtraction()
    {
        Instant testDate = Instant.parse("2026-04-09T17:21:18.392850600Z");
        int sec = ChronoFieldUtil.valueOf(testDate, ZoneId.systemDefault(), ChronoField.SECOND_OF_MINUTE);
        int min = ChronoFieldUtil.valueOf(testDate, ZoneId.systemDefault(), ChronoField.MINUTE_OF_HOUR);
        int hr = ChronoFieldUtil.valueOf(testDate, ZoneId.systemDefault(), ChronoField.HOUR_OF_DAY);
        int day = ChronoFieldUtil.valueOf(testDate, ZoneId.systemDefault(), ChronoField.DAY_OF_WEEK);
        Assertions.assertEquals(sec, 18);
        Assertions.assertEquals(min, 21);
        Assertions.assertEquals(hr, 19);
        Assertions.assertEquals(day, 4); //Thursday
    }

    @Test
    public void convert()
    {
        System.out.println(Instant.now());

    }

}