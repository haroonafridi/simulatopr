package com.hkcapital.portoflio.indicators;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalField;

/**
 * Utility class for extracting different temporal fields from the date component.
 */
public final class ChronoFieldUtil
{

    private ChronoFieldUtil()
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
        return ZonedDateTime.ofInstant(instant, zoneId).get(temporalField);
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

    public static int getTimeFrame(Instant instant, ZoneId zoneId, TemporalField temporalField)
    {
        return valueOf(instant, zoneId, temporalField);
    }

    public static long toBucket(Instant time, Unit unit, int interval)
    {
        long epochMillis = time.toEpochMilli();

        long bucketSizeMillis = switch (unit)
                {
                    case MINUTE -> interval * 60_000L;
                    case HOUR -> interval * 60 * 60_000L;
                    case DAY    -> interval * 24 * 60 * 60_000L;
                    case WEEK   -> interval * 7 * 24 * 60 * 60_000L;
                };
        return epochMillis / bucketSizeMillis;
    }

    public static Instant bucketStart(Instant time, Unit unit, int interval)
    {
        long bucket = toBucket(time, unit, interval);

        long bucketSizeMillis = switch (unit)
                {
                    case MINUTE -> interval * 60_000L;
                    case HOUR -> interval * 60 * 60_000L;
                    case DAY -> 0L;
                    case WEEK -> 0L;
                };
        return Instant.ofEpochMilli(bucket * bucketSizeMillis);
    }

    public static Instant parse(String input) {
         return LocalDateTime.parse(input, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS")) //
                 .toInstant(ZoneOffset.UTC);
    }
}
