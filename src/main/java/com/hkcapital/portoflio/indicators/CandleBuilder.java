package com.hkcapital.portoflio.indicators;

import java.time.ZoneId;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.hkcapital.portoflio.indicators.TimeUtil.minuteOfHour;
import static java.lang.Math.max;
import static java.lang.Math.min;

public class CandleBuilder
{
    private CandleList candles = new CandleList();

    private CandleBuilder()
    {
    }

    public static CandleBuilder build()
    {
        return new CandleBuilder();
    }

    public CandleBuilder addTick(final Tick tick)
    {
        if (candles.isEmpty())
        {
            addCandle(tick);
        }
        Candle candle = candles.get(candles.size() - 1);

        if (isSameTimeFrame(candle, tick))
        {
            Candle updatedCandle = updateCandle(candle, tick);
            candles.set(candles.size() - 1, updatedCandle);
        } else
        {
            addCandle(tick);
        }
        return this;
    }

    private Candle updateCandle(final Candle candle, final Tick tick)
    {
        candle.setLow(min(candle.getLow(), tick.getVal()));
        candle.setHigh(max(candle.getHigh(), tick.getVal()));
        candle.setClose(tick.getVal());
        candle.setTime(tick.getTime());
        return candle;
    }

    private void addCandle(final Tick tick)
    {
        candles.add(new Candle(tick.getInstrument(),
                tick.getVal(), tick.getVal(), tick.getVal(),
                tick.getVal(), tick.getTime(), Unit.MINUTE, 1));
    }

    private boolean isSameTimeFrame(final Candle candle, final Tick tick)
    {
        return minuteOfHour(candle.getTime(), ZoneId.systemDefault()) ==
                minuteOfHour(tick.getTime(), ZoneId.systemDefault());
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
