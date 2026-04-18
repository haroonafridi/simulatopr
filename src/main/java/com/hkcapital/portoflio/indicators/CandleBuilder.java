package com.hkcapital.portoflio.indicators;

import java.time.ZoneId;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalField;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.hkcapital.portoflio.indicators.ChronoFieldUtil.getTimeFrame;
import static java.lang.Math.max;
import static java.lang.Math.min;

public class CandleBuilder
{
    private CandleList candles = new CandleList();
    private TemporalField muinuteTimeFrame = ChronoField.MINUTE_OF_HOUR;

    private CandleBuilder()
    {
    }

    public static CandleBuilder build()
    {
        return new CandleBuilder();
    }

    public CandleBuilder addAndUpdateCandle(final Candle subcandle, Unit timeUnit, Integer interval)
    {
        if (candles.isEmpty())
        {
            addCandle(subcandle, timeUnit, interval);
        }
        Candle mainCandle = candles.get(candles.size() - 1);
        if (isSameTimeFrame(mainCandle, subcandle))
        {
            Candle updatedCandle = updateCandle(mainCandle, subcandle);
            candles.set(candles.size() - 1, updatedCandle);
        } else
        {
            addCandle(subcandle, timeUnit, interval);
        }
        return this;
    }

    private Candle updateCandle(final Candle candle, final Candle subCandle)
    {
        candle.setLow(min(candle.getLow(), subCandle.getLow()));
        candle.setHigh(max(candle.getHigh(), subCandle.getHigh()));
        candle.setClose(subCandle.getClose());
        candle.setTime(subCandle.getTime());
        return candle;
    }

    private void addCandle(final Candle subCandle, Unit timeUnit, Integer interval)
    {
        candles.add(new Candle(subCandle.getInstrument(),
                subCandle.getOpen(), subCandle.getLow(), subCandle.getHigh(),
                subCandle.getClose(), subCandle.getTime(), timeUnit, interval));
    }

    private boolean isSameTimeFrame(final Candle candle, final Candle subCandle)
    {
        int mainCandleTimeFrame = getTimeFrame(candle.getTime(), ZoneId.systemDefault(), muinuteTimeFrame);
        int subCandleTimeFrame = getTimeFrame(subCandle.getTime(), ZoneId.systemDefault(), muinuteTimeFrame);
        return mainCandleTimeFrame == subCandleTimeFrame;
    }

    public List<Candle> fromTo(final Unit unit, final Integer range)
    {
        List<Candle> candleList = candles.stream()
                .filter(candle -> candle.getUnit().equals(unit)) //
                .collect(Collectors.toList());
        if (candleList.isEmpty())
        {
            return Collections.emptyList();
        }
        int size = candles.size();
        int fromIndex = max(size - range, 0);
        return candles.subList(fromIndex, size);
    }

    public List<Candle> of(Unit unit)
    {
        return candles.stream()
                .filter(candle -> candle.getUnit().equals(unit)) //
                .collect(Collectors.toList());
    }
}
