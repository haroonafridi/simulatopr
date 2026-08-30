package com.hkcapital.portflio.ui.chart;


import com.hkcapital.portflio.market.indicators.CandleBuilder;
import com.hkcapital.portflio.market.indicators.CandleDto;
import com.hkcapital.portflio.market.indicators.TimeFramesUnit;
import com.hkcapital.portflio.model.Candle;
import com.hkcapital.portflio.service.candle.etoro.impl.SignalBuilder;
import org.jfree.chart.axis.DateTickUnit;
import org.jfree.chart.axis.DateTickUnitType;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.data.time.DateRange;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class ChartUtil
{
    public static DateRange createDateRange(TimeFramesUnit timeFramesUnit,
                                            Integer interval,
                                            SignalBuilder signalBuilder
                                            )
    {
        LocalDate today = LocalDate.now();

        Date startOfDay = Date.from(today.atStartOfDay(ZoneId.systemDefault())
                .plusDays(-2)
                .toInstant());

        Date endOfDay = Date.from(today.plusDays(2)
                .atTime(23, 59, 59)
                .atZone(ZoneId.systemDefault()).toInstant());

        if (TimeFramesUnit.MINUTE.equals(timeFramesUnit))
        {
            switch (interval)
            {
                case 1:
                {
                   CandleDto candle = null;
//                           signalBuilder.getCandleBuilder1Min().getCandles()
//                                   .get(signalBuilder.getCandleBuilder1Min()
//                                           .getCandles().size());

                    LocalDate td = candle.getHighTime().atZone(ZoneId.systemDefault()).toLocalDate().plusDays(-1);


                    today.atStartOfDay(ZoneId.systemDefault()).plusDays(-1).toInstant();

                    //startOfDay = Date.from();

                    endOfDay = Date.from(today.plusDays(1)
                            .atTime(23, 59, 59)
                            .atZone(ZoneId.systemDefault()).toInstant());
                    break;
                }
                default:
                {
                    startOfDay = Date.from(today.atStartOfDay(ZoneId.systemDefault())
                            .plusDays(-2)
                            .toInstant());

                    endOfDay = Date.from(today.plusDays(2)
                            .atTime(23, 59, 59)
                            .atZone(ZoneId.systemDefault()).toInstant());
                    break;
                }

            }
        }

        if (TimeFramesUnit.HOUR.equals(timeFramesUnit))
        {
            switch (interval)
            {
                case 1:
                {
                    startOfDay = Date.from(today.atStartOfDay(ZoneId.systemDefault())
                            .plusDays(-4)
                            .toInstant());

                    endOfDay = Date.from(today.plusDays(4)
                            .atTime(23, 59, 59)
                            .atZone(ZoneId.systemDefault()).toInstant());
                    break;
                }

                default:
                {
                    startOfDay = Date.from(today.atStartOfDay(ZoneId.systemDefault())
                            .plusDays(-6)
                            .toInstant());

                    endOfDay = Date.from(today.plusDays(6)
                            .atTime(23, 59, 59)
                            .atZone(ZoneId.systemDefault()).toInstant());
                    break;
                }
            }
        }

        return new DateRange(startOfDay, endOfDay);
    }

    public static NumberTickUnit createYaxisNumberTickUnit(TimeFramesUnit timeFramesUnit,
                                                           Integer interval)
    {
        NumberTickUnit tickUnit = null;
        if (TimeFramesUnit.MINUTE.equals(timeFramesUnit))
        {
            switch (interval)
            {
                case 1:
                {
                    tickUnit = new NumberTickUnit(5);
                    break;
                }
                case 5:
                {
                    tickUnit = new NumberTickUnit(10);
                    break;
                }
                case 15:
                {
                    tickUnit = new NumberTickUnit(20);
                    break;
                }
                default:
                {
                    tickUnit = new NumberTickUnit(40);
                    break;
                }
            }
        }
        if (TimeFramesUnit.HOUR.equals(timeFramesUnit))
        {
            switch (interval)
            {
                case 1:
                {
                    tickUnit = new NumberTickUnit(40);
                    break;
                }
                default:
                {
                    tickUnit = new NumberTickUnit(100);
                    break;
                }
            }
        }
        return tickUnit;
    }


    public static DateTickUnit createXaxisNumberTickUnit(TimeFramesUnit timeFramesUnit, Integer interval)
    {
        DateTickUnit tickUnit = null;
        if (TimeFramesUnit.MINUTE.equals(timeFramesUnit))
        {
            switch (interval)
            {
                case 1:
                {
                    tickUnit = new DateTickUnit(DateTickUnitType.MINUTE, 2*60);
                    break;
                }
                case 5:
                {
                    tickUnit = new DateTickUnit(DateTickUnitType.MINUTE, 3 * 60);
                    break;
                }
                case 15:
                {
                    tickUnit = new DateTickUnit(DateTickUnitType.MINUTE, 6 * 60);
                    break;
                }
                default:
                {
                    tickUnit = new DateTickUnit(DateTickUnitType.MINUTE, 12 * 60);
                    break;
                }
            }
        }

        if (TimeFramesUnit.HOUR.equals(timeFramesUnit))
        {
            switch (interval)
            {
                case 1:
                {
                    tickUnit = new DateTickUnit(DateTickUnitType.MINUTE, 3 * 24 * 60);
                    break;
                }

                default:
                {
                    tickUnit = new DateTickUnit(DateTickUnitType.MINUTE, 7 * 24 * 60);
                    break;
                }
            }
        }
        return tickUnit;
    }
}
