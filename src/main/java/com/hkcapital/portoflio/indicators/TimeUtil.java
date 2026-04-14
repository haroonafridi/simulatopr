package com.hkcapital.portoflio.indicators;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalField;

/**
 * Utility class for extracting different temporal fields from the date component.
 */
public final class TimeUtil
{

    private TimeUtil()
    {
    }

    /**
     * @param instant       {@link Instant}
     * @param zoneId        {@link ZoneId}
     * @param temporalField {@link  TemporalField}
     * @return numeric value extracted from date component e.g minute, sec etc
     */

    public static int valueOf(Instant instant, ZoneId zoneId, TemporalField temporalField)
    {
        return ZonedDateTime.ofInstant(instant, zoneId.systemDefault()).get(temporalField);
    }

    /**
     * @param instant {@link Instant}
     * @param zoneId  {@link  ZoneId}
     * @return will return minute from the hour component of the date component
     */
    public static int minuteOfHour(Instant instant, ZoneId zoneId)
    {
        return valueOf(instant, zoneId, ChronoField.MINUTE_OF_HOUR);
    }

    /**
     * @param instant {@link Instant}
     * @param zoneId  {@link  ZoneId}
     * @return will return second from the minute component of the date component
     */
    public static int secondOfMinute(Instant instant, ZoneId zoneId)
    {
        return valueOf(instant, zoneId, ChronoField.SECOND_OF_MINUTE);
    }

    /**
     * @param instant
     * @param zoneId
     * @return
     */
    public static int hourOfDay(Instant instant, ZoneId zoneId)
    {
        return valueOf(instant, zoneId, ChronoField.HOUR_OF_DAY);
    }

    /**
     * @param instant
     * @param zoneId
     * @return
     */
    public static int dayOfWeek(Instant instant, ZoneId zoneId)
    {
        return valueOf(instant, zoneId, ChronoField.DAY_OF_WEEK);
    }

    /**
     * @param instant
     * @param zoneId
     * @return
     */
    public static int weekOfMonth(Instant instant, ZoneId zoneId)
    {
        return valueOf(instant, zoneId, ChronoField.ALIGNED_WEEK_OF_MONTH);
    }

}
