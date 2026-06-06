package com.hkcapital.portflio.market.structure;

import com.hkcapital.portflio.market.indicators.TimeFramesUnit;

import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDateTime;

import java.time.format.DateTimeFormatter;

public class DateTimeUtil
{
    public static Day toDay() //
    {
        DayOfWeek dayOfWeek = LocalDateTime.now().getDayOfWeek();
        switch (dayOfWeek)
        {
            case MONDAY ->
            {
                return Day.MONDAY;
            }

            case TUESDAY ->
            {
                return Day.TUESDAY;
            }

            case WEDNESDAY ->
            {
                return Day.WEDENSDAY;
            }

            case THURSDAY ->
            {
                return Day.THURSDAY;
            }

            case FRIDAY ->
            {
                return Day.FRIDAY;
            }

            case SATURDAY ->
            {
                return Day.SATURDAY;
            }

            case SUNDAY ->
            {
                return Day.SUNDAY;
            }
            default -> throw new RuntimeException("Invalid day of the week");
        }

    }

    public static Long minus(Instant d2, Instant d1, TimeFramesUnit timeFramesUnit)
    {
        final long i2 =  d2.toEpochMilli();

        final long i1 = d1.toEpochMilli();

        if (TimeFramesUnit.SECOND.equals(timeFramesUnit))
        {
            return Math.abs(i2 - i1) / 1000;
        }

        if (TimeFramesUnit.MINUTE.equals(timeFramesUnit))
        {
            return  Math.abs(i2 - i1) / 60000;
        }

        if (TimeFramesUnit.HOUR.equals(timeFramesUnit))
        {
            return  Math.abs(i2 - i1) / (60000 * 60);
        }
        throw new IllegalArgumentException("Not yet implemented or invalid TimeFramesUnit");
    }

    public static LocalDateTime localDateTimeFrom(String input) {
        return LocalDateTime.parse(input, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS"));
    }

    public static Instant toInstant(String input) {
        return Instant.parse(input);
    }
}
