package com.hkcapital.portoflio.indicators;

import com.hkcapital.portoflio.service.api.etoro.websocket.LiveInstrumentRate;
import com.hkcapital.portoflio.service.candle.etoro.EtoroCandleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.Math.max;
import static java.lang.Math.min;

@Component
public class CandleBuilder
{
    private Logger logger = LoggerFactory.getLogger(CandleBuilder.class);
    private CandleList candles = new CandleList();
    private Unit timeFrame;
    private Integer interval;
    private RSI rsi = new RSI();
    private final ATR atr = new ATR(14);
    private final EMA ema = new EMA(14);
    private final SMA sma = new SMA(14);
    private EtoroCandleService candleService = null;

    public CandleBuilder(EtoroCandleService candleService)
    {
        this.candleService = candleService;
    }

    public EtoroCandleService getCandleService()
    {
        return candleService;
    }

    private CandleBuilder()
    {
    }

    public void setCandleService(EtoroCandleService candleService)
    {
        this.candleService = candleService;
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
            return this;
        }
        Candle mainCandle = candles.get(candles.size() - 1);
        if (isSameTimeFrame(mainCandle, subcandle))
        {
            Candle updatedCandle = updateCandle(mainCandle, subcandle);
            candles.set(candles.size() - 1, updatedCandle);
        } else
        {
            Candle closedCandle = mainCandle;
            addCandle(subcandle, timeFrame, interval);
            com.hkcapital.portoflio.model.Candle etoroCandle = com.hkcapital.portoflio.model
                    .Candle.builder()
                    .instrumentID(Integer.parseInt(closedCandle.getInstrument()))
                    .close(closedCandle.getClose())
                    .low(closedCandle.getLow())
                    .high(closedCandle.getHigh())
                    .open(closedCandle.getOpen())
                    .fromDate(closedCandle.getTime())
                    .creationDateTime(LocalDateTime.now())
                    .timeFrame(closedCandle.getUnit().getUnit() + "-" + closedCandle.getInterval())
                    .build();
            candleService.save(etoroCandle);
            Double rsiValue = rsi.onCandleAdd(closedCandle, 14);
            Double atrVal = atr.onCandleAdd(closedCandle);
            Double emaVal = ema.onPrice(closedCandle.getClose());
            Double smaVal = sma.onPrice(closedCandle.getClose());

            if (rsiValue == null)
            {
                rsiValue = 0d;
            }

            if (atrVal == null)
            {
                atrVal = 0d;
            }

            if (emaVal == null)
            {
                emaVal = 0d;
            }
            if (smaVal == null)
            {
                smaVal = 0d;
            }

            logger.info("Candle closed event fired: rsi = {}  atr = {}, ema = {} sma = {}, {} ", rsiValue, atrVal, emaVal, smaVal, closedCandle);
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

    public void flush()
    {
        if (!candles.isEmpty())
        {
            Candle lastCandle = candles.get(candles.size() - 1);
            //publishCloseEvent(lastCandle);
        }
    }
}
