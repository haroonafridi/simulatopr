package com.hkcapital.portflio.market.indicators;
import com.hkcapital.portflio.market.structure.MarketStructure;
import com.hkcapital.portflio.market.structure.MarketStructureManagerCache;
import com.hkcapital.portflio.market.structure.MarketTypes;
import com.hkcapital.portflio.model.Candle;
import com.hkcapital.portflio.model.CandleSource;
import com.hkcapital.portflio.service.api.etoro.websocket.LiveInstrumentRate;
import com.hkcapital.portflio.service.candle.etoro.EtoroCandleService;
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
    private TimeFramesUnit timeFrame;
    private Integer interval;
    private RSI rsi = new RSI();
    private final ATR atr = new ATR(14);
    private final EMA ema = new EMA(14);
    private final SMA sma = new SMA(14);
    private EtoroCandleService candleService = null;
    private MarketStructureManagerCache marketStructureManagerCache;

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

    public CandleBuilder addAndUpdateCandle(final CandleDto subcandle)
    {
        if (candles.isEmpty())
        {
            addCandle(subcandle, timeFrame, interval);
            return this;
        }
        CandleDto mainCandle = candles.get(candles.size() - 1);
        if (isSameTimeFrame(mainCandle, subcandle))
        {
            CandleDto updatedCandle = updateCandle(mainCandle, subcandle);
            candles.set(candles.size() - 1, updatedCandle);
        } else
        {
            CandleDto closedCandle = mainCandle;
            addCandle(subcandle, timeFrame, interval);
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
            Candle candle = com.hkcapital.portflio.model
                    .Candle.builder()
                    .instrumentID(Integer.parseInt(closedCandle.getInstrument()))
                    .close(closedCandle.getClose())
                    .low(closedCandle.getLow())
                    .high(closedCandle.getHigh())
                    .open(closedCandle.getOpen())
                    .fromDate(closedCandle.getTime())
                    .atr(atrVal)
                    .ema(emaVal)
                    .rsi(rsiValue)
                    .sma(smaVal)
                    .creationDateTime(LocalDateTime.now())
                    .timeFrame(closedCandle.getInterval())
                    .timeFrameUnit(closedCandle.getTimeFramesUnit().getUnit())
                    .source(CandleSource.INTERNAL.getSource())
                    .build();

            MarketStructure structure = marketStructureManagerCache.get(MarketTypes.GOLD_15_MIN);

            if (structure != null && candle.getTimeFrame() == 15)
            {
                structure.createOrUpdateBands(candle);
            }
            candleService.save(candle);
            logger.info("Candle closed event fired: rsi = {}  atr = {}, ema = {} sma = {}, {} ", rsiValue, atrVal, emaVal, smaVal, closedCandle);
        }
        return this;
    }


    private CandleDto updateCandle(final CandleDto candle, final CandleDto subCandle)
    {
        candle.setLow(min(candle.getLow(), subCandle.getLow()));
        candle.setHigh(max(candle.getHigh(), subCandle.getHigh()));
        candle.setClose(subCandle.getClose());
        candle.setTime(subCandle.getTime());
        return candle;
    }

    private void addCandle(final CandleDto subCandle, TimeFramesUnit timeTimeFramesUnit, Integer interval)
    {
        Instant bucketTime = ChronoFieldUtil.bucketStart(subCandle.getTime(), subCandle.getTimeFramesUnit(), interval);
        candles.add(new CandleDto(subCandle.getInstrument(),
                subCandle.getOpen(), subCandle.getLow(), subCandle.getHigh(),
                subCandle.getClose(), bucketTime.truncatedTo(ChronoUnit.SECONDS), timeTimeFramesUnit, interval));
    }

    public CandleBuilder ofTimeFrame(TimeFramesUnit timeFrame)
    {
        this.timeFrame = timeFrame;
        return this;
    }

    public CandleBuilder ofInterval(Integer interval)
    {
        this.interval = interval;
        return this;
    }


    public CandleBuilder marketStructureManagerCache(MarketStructureManagerCache marketStructureManagerCache)
    {
        this.marketStructureManagerCache = marketStructureManagerCache;
        return this;
    }

    public List<CandleDto> fromTo(final TimeFramesUnit timeFramesUnit, final Integer range)
    {
        List<CandleDto> candleList = candles.stream()
                .filter(candle -> candle.getTimeFramesUnit().equals(timeFramesUnit)) //
                .collect(Collectors.toList());
        if (candleList.isEmpty())
        {
            return Collections.emptyList();
        }
        int size = candles.size();
        int fromIndex = max(size - range, 0);
        return candles.subList(fromIndex, size);
    }

    public List<CandleDto> candles()
    {
        return candles.stream()
                .filter(candle -> candle.getTimeFramesUnit().equals(this.timeFrame)) //
                .collect(Collectors.toList());
    }

    private boolean isSameTimeFrame(CandleDto c1, CandleDto c2)
    {
        long bucket1 = ChronoFieldUtil.toBucket(c1.getTime(), c1.getTimeFramesUnit(), c1.getInterval());
        long bucket2 = ChronoFieldUtil.toBucket(c2.getTime(), c2.getTimeFramesUnit(), c2.getInterval());
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
            CandleDto lastCandle = candles.get(candles.size() - 1);
            //publishCloseEvent(lastCandle);
        }
    }

    public CandleList getCandles()
    {
        return candles;
    }

    public TimeFramesUnit getTimeFrame()
    {
        return timeFrame;
    }

    public Integer getInterval()
    {
        return interval;
    }

    public RSI getRsi()
    {
        return rsi;
    }

    public ATR getAtr()
    {
        return atr;
    }

    public EMA getEma()
    {
        return ema;
    }

    public SMA getSma()
    {
        return sma;
    }
}
