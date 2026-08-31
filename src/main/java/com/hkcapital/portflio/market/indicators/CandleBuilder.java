package com.hkcapital.portflio.market.indicators;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hkcapital.portflio.market.structure.MarketStructure;
import com.hkcapital.portflio.market.structure.MarketStructureCache;
import com.hkcapital.portflio.model.BandLogger;
import com.hkcapital.portflio.model.Candle;
import com.hkcapital.portflio.model.CandleSource;
import com.hkcapital.portflio.model.Instrument;
import com.hkcapital.portflio.service.api.etoro.websocket.LiveInstrumentRate;
import com.hkcapital.portflio.service.bandlogger.Bandlogger;
import com.hkcapital.portflio.service.candle.etoro.EtoroCandleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static java.lang.Math.max;

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
    private EtoroCandleService candleService;

    private Instrument instrument;
    private MarketStructureCache marketStructureManagerCache;

    private Bandlogger bandlogger;

    private ObjectMapper objectMapper;

    public CandleBuilder(EtoroCandleService candleService, Instrument instrument)
    {
        this.candleService = candleService;
        this.instrument = instrument;
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


    public void setInstrument(Instrument instrument)
    {
        this.instrument = instrument;
    }

    public static CandleBuilder build()
    {
        return new CandleBuilder();
    }

    public CandleBuilder addAndUpdateCandle(final CandleDto subCandle)
    {
        if (candles.isEmpty())
        {
            addCandle(subCandle, timeFrame, interval);
            return this;
        }
        CandleDto mainCandle = candles.get(candles.size() - 1);
        if (isSameTimeFrame(mainCandle, subCandle))
        {
            CandleDto updatedCandle = updateCandle(mainCandle, subCandle);
            candles.set(candles.size() - 1, updatedCandle);
        } //
        else
        {
            CandleDto closedCandle = mainCandle;
            addCandle(subCandle, timeFrame, interval);
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
                    .lowTime(closedCandle.getLowTime())
                    .high(closedCandle.getHigh())
                    .highTime(closedCandle.getHighTime())
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

            marketStructureManagerCache.getStructures().entrySet().forEach(entry ->
            {
                MarketStructure structure = entry.getValue();
                if (structure != null)
                {
                    switch (candle.getTimeFrameUnit())
                    {
                        case "HOUR":
                        {
                            switch (candle.getTimeFrame())
                            {
                                case 4:
                                    createAndSaveStructure(structure, candle, structure);
                                    break;
                                case 1:
                                    createAndSaveStructure(structure.getChildMarketStructure(), candle, structure);
                                    break;
                                default:
                                    break;
                            }
                        }

                        case "MINUTE":
                        {
                            switch (candle.getTimeFrame())
                            {
                                case 30:
                                    createAndSaveStructure(structure.getChildMarketStructure().getChildMarketStructure(), candle, structure);
                                    break;
                                case 15:
                                    createAndSaveStructure(structure.getChildMarketStructure() //
                                            .getChildMarketStructure().getChildMarketStructure(), candle, structure);
                                    break;
                                case 5:
                                    createAndSaveStructure(structure.getChildMarketStructure() //
                                            .getChildMarketStructure() //
                                            .getChildMarketStructure() //
                                            .getChildMarketStructure(), candle, structure);
                                    break;
                                case 1:
                                    createAndSaveStructure(structure.getChildMarketStructure() //
                                            .getChildMarketStructure() //
                                            .getChildMarketStructure() //
                                            .getChildMarketStructure() //
                                            .getChildMarketStructure(), candle, structure);
                                    break;
                                default:
                                    break;
                            }
                        }
                        default:
                            break;
                    }
                }
            });

            candleService.save(candle);
            logger.info("Candle closed event fired: rsi = {}  atr = {}, ema = {} sma = {}, {} ", rsiValue, atrVal, emaVal, smaVal, closedCandle);
        }
        return this;
    }

    private void createAndSaveStructure(MarketStructure childStructure, Candle candle, MarketStructure mainStructure)
    {
        childStructure.createOrUpdateBands(candle);
        try
        {
            saveMarketStructure(mainStructure);
        } catch (JsonProcessingException e)
        {
            throw new RuntimeException(e);
        }
    }

    private void saveMarketStructure(MarketStructure structure) throws JsonProcessingException
    {
        String uuid = UUID.randomUUID().toString();

        // 1. Recursively convert the hierarchical structure into a nested ObjectNode
        String band = marketStructureExtractor(structure, uuid);
        BandLogger bandLoggerEntity = new BandLogger();
        bandLoggerEntity.setUuid(uuid);
        bandLoggerEntity.setBandDesc(band); // String serialization is handled above
        bandLoggerEntity.setCreationDate(LocalDateTime.now());
        bandlogger.save(bandLoggerEntity);
    }

    private String marketStructureExtractor(MarketStructure structure, String uuid)
            throws JsonProcessingException
    {
        ObjectNode marketStructureJson = convertStructureToNode(structure);
        // 2. Wrap it in your main root logger payload
        ObjectNode root = objectMapper.createObjectNode();
        ObjectNode priceRange = objectMapper.createObjectNode();
        priceRange.put("low", structure.getPriceRange().getLow());
        priceRange.put("high", structure.getPriceRange().getHigh());

        root.put("uuid", uuid);
        root.put("ticker", structure.getInstrument().getInstrumentTicker());
        root.put("marketDate", structure.getMarketDate().format(DateTimeFormatter.ISO_DATE));
        root.put("creationDate", structure.getCreationDate().format(DateTimeFormatter.ISO_DATE));
        root.put("previousDayRange", priceRange);
        root.set("marketStructure", marketStructureJson);
        // 3. Serialize to string and save to the database
        String band = objectMapper.writeValueAsString(root);
        return band;
    }

    private CandleDto updateCandle(final CandleDto candle, final CandleDto subCandle)
    {
        if (subCandle.getLow() < candle.getLow())
        {
            candle.setLow(subCandle.getLow());
            candle.setLowTime(subCandle.getLowTime());
        }

        if (subCandle.getHigh() > candle.getHigh())
        {
            candle.setHigh(subCandle.getHigh());
            candle.setHighTime(subCandle.getHighTime());
        }
        candle.setClose(subCandle.getClose());
        candle.setTime(subCandle.getTime());
        return candle;
    }

    private void addCandle(final CandleDto subCandle, TimeFramesUnit timeTimeFramesUnit, Integer interval)
    {
        if (subCandle.getTime() != null && subCandle.getTimeFramesUnit() != null && interval != null)
        {
            Instant bucketTime = ChronoFieldUtil.bucketStart(subCandle.getTime(), subCandle.getTimeFramesUnit(), interval);

            candles.add(new CandleDto(subCandle.getInstrument(),
                    subCandle.getOpen(), subCandle.getLow(), subCandle.getTime(), subCandle.getHigh(),
                    subCandle.getTime(), subCandle.getClose(),
                    bucketTime.truncatedTo(ChronoUnit.SECONDS), timeTimeFramesUnit, interval));
        }

    }

    public CandleBuilder ofTimeFrame(TimeFramesUnit timeFrame)
    {
        this.timeFrame = timeFrame;
        return this;
    }

    public CandleBuilder objectMapper(ObjectMapper objectMapper)
    {
        this.objectMapper = objectMapper;
        return this;
    }

    public CandleBuilder bandLogger(Bandlogger bandlogger)
    {
        this.bandlogger = bandlogger;
        return this;
    }

    public CandleBuilder ofInterval(Integer interval)
    {
        this.interval = interval;
        return this;
    }


    public CandleBuilder marketStructureManagerCache(MarketStructureCache marketStructureManagerCache)
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
        }
    }


    private ObjectNode convertStructureToNode(MarketStructure structure)
    {
        if (structure == null)
        {
            return null;
        }
        ObjectNode node = objectMapper.createObjectNode();
        // Map current level attributes dynamically
        // (Assuming structure has a way to expose its timeframe context, e.g., structure.getTimeframe())
        node.put("timeFrame", structure.getTimeFrame());
        node.set("timeFrameUnit", objectMapper.valueToTree(structure.getTimeFrameUnit()));
        node.set("lowerBands", objectMapper.valueToTree(structure.getLowerBands()));
        node.set("upperBands", objectMapper.valueToTree(structure.getUpperBands()));
        // RECURSION: If a child structure exists, nest it inside this node
        if (structure.getChildMarketStructure() != null)
        {
            node.set("child", convertStructureToNode(structure.getChildMarketStructure()));
        }
        return node;
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

    public Instrument getInstrument()
    {
        return instrument;
    }
}


