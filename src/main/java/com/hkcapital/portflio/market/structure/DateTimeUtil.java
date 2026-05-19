package com.hkcapital.portflio.market.structure;

import java.time.DayOfWeek;
import java.time.LocalDateTime;

public class DateTimeUtil
{
    public static Day toDay() //
    {
        DayOfWeek dayOfWeek =  LocalDateTime.now().getDayOfWeek();
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
}
