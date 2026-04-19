package com.hkcapital.portoflio.indicators;

import com.hkcapital.portoflio.service.api.etoro.websocket.LiveInstrumentRate;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class CandleBuilder
{
    private CandleList candles = new CandleList();
    private Unit timeFrame;
    private Integer interval;

    private CandleBuilder()
    {
    }

    public static CandleBuilder build()
    {
        return new CandleBuilder();
    }

    public CandleBuilder addAndUpdateCandle(final Candle subcandle)
    {

        if (candles.isEmpty())
        {
            addCandle(subcandle, timeFrame, interval);
        }
        Candle mainCandle = candles.get(candles.size() - 1);
        if (isSameTimeFrame(mainCandle, subcandle))
        {
            Candle updatedCandle = updateCandle(mainCandle, subcandle);
            candles.set(candles.size() - 1, updatedCandle);
        } else
        {
            addCandle(subcandle, timeFrame, interval);
            // candle closed event logic here
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
        Instant bucketTime = ChronoFieldUtil.bucketStart(subCandle.getTime(), subCandle.getUnit(), interval);
        candles.add(new Candle(subCandle.getInstrument(),
                subCandle.getOpen(), subCandle.getLow(), subCandle.getHigh(),
                subCandle.getClose(), bucketTime.truncatedTo(ChronoUnit.SECONDS), timeUnit, interval));
    }


    public CandleBuilder ofTimeFrame(Unit timeFrame)
    {
        this.timeFrame = timeFrame;
        return this;
    }

    public CandleBuilder ofInterval(Integer interval)
    {
        this.interval = interval;
        return this;
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

    public List<Candle> candles()
    {
        return candles.stream()
                .filter(candle -> candle.getUnit().equals(this.timeFrame)) //
                .collect(Collectors.toList());
    }

    private boolean isSameTimeFrame(Candle c1, Candle c2)
    {
        long bucket1 = ChronoFieldUtil.toBucket(c1.getTime(), c1.getUnit(), c1.getInterval());
        long bucket2 = ChronoFieldUtil.toBucket(c2.getTime(), c2.getUnit(), c2.getInterval());
        return bucket1 == bucket2;
    }

    public static Tick tickFromRate(final LiveInstrumentRate rate)
    {
        return Tick.builder().instrument(rate.getInstrumentId().toString()) //
                .time(rate.getDate())//
                .val(rate.getAsk()) //
                .build();
    }
}
