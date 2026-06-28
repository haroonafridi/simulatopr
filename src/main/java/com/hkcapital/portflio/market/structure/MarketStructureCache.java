package com.hkcapital.portflio.market.structure;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hkcapital.portflio.market.indicators.TimeFramesUnit;
import com.hkcapital.portflio.model.Candle;
import com.hkcapital.portflio.model.Instrument;
import com.hkcapital.portflio.service.candle.etoro.EtoroCandleService;
import com.hkcapital.portflio.service.instrument.InstrumentService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import com.hkcapital.portflio.service.registry.Service;

@Component
@Slf4j
public class MarketStructureCache implements Service
{
    private Logger logger = LoggerFactory.getLogger(MarketStructureCache.class);
    private final Map<MarketTypes, MarketStructure> structures =
            new ConcurrentHashMap<>();
    private final EtoroCandleService candleService;
    private final InstrumentService instrumentService;

    private final ObjectMapper objectMapper;

    public MarketStructureCache(final EtoroCandleService candleService,
                                final InstrumentService instrumentService,
                                final ObjectMapper objectMapper)
    {
        this.candleService = candleService;
        this.instrumentService = instrumentService;
        this.objectMapper = objectMapper;
    }

    public void register(MarketTypes key, MarketStructure structure)
    {
        structures.put(key, structure);
    }

    public MarketStructure get(MarketTypes key)
    {
        return structures.get(key);
    }
    @Scheduled(cron = "0 0 23 * * MON-FRI")
    public void closeMarket()
    {
        logger.info("Flushing cache");
        if (structures.get(MarketTypes.GOLD_15_MIN) != null)
        {
            structures.get(MarketTypes.GOLD_15_MIN).flush();
        }
    }

    @Scheduled(cron = "0 0 0 * * TUE-SAT")
    public void openMarket()
    {
        logger.info("Creating market cache..");


        LocalDate today = LocalDate.now();

        DayOfWeek day = today.getDayOfWeek();

        LocalDate targetDate;

        if (day == DayOfWeek.MONDAY) {
            targetDate = today.minusDays(3); // Friday
        } else if (day == DayOfWeek.SUNDAY) {
            targetDate = today.minusDays(2); // Friday
        } else if (day == DayOfWeek.SATURDAY) {
            targetDate = today.minusDays(1); // Friday
        } else {
            targetDate = today.minusDays(1); // Yesterday
        }

        LocalDateTime start = targetDate.atStartOfDay();

        LocalDateTime end = targetDate
                .plusDays(1)
                .atStartOfDay()
                .minusNanos(1);

        List<Candle> candleList1Mins = candleService.findByInstrumentIDAndTimeFrameAndTimeFrameUnitAndCreationDateTimeBetween(18, 1,
                TimeFramesUnit.MINUTE.getUnit(), start, end);

        List<Candle> candleList5Mins = candleService.findByInstrumentIDAndTimeFrameAndTimeFrameUnitAndCreationDateTimeBetween(18, 5,
                TimeFramesUnit.MINUTE.getUnit(), start, end);

        List<Candle> candleList15Mins = candleService.findByInstrumentIDAndTimeFrameAndTimeFrameUnitAndCreationDateTimeBetween(18, 15,
                TimeFramesUnit.MINUTE.getUnit(), start, end);

        List<Candle> candleList30Mins = candleService.findByInstrumentIDAndTimeFrameAndTimeFrameUnitAndCreationDateTimeBetween(18, 30,
                TimeFramesUnit.MINUTE.getUnit(), start, end);

        List<Candle> candleList1Hour = candleService.findByInstrumentIDAndTimeFrameAndTimeFrameUnitAndCreationDateTimeBetween(18, 1,
                TimeFramesUnit.HOUR.getUnit(), start, end);

        List<Candle> candleList4Hour = candleService.findByInstrumentIDAndTimeFrameAndTimeFrameUnitAndCreationDateTimeBetween(18, 4,
                TimeFramesUnit.HOUR.getUnit(), start, end);

        if(candleList15Mins.size() == 0)
        {
            logger.info("No candle data found cannot create market structure");
            return;
        }
        double low = candleList15Mins.stream().mapToDouble(c -> c.getLow()).min().getAsDouble();
        double high = candleList15Mins.stream().mapToDouble(c -> c.getHigh()).max().getAsDouble();
        logger.info("Day range created low = {} , high = {}",low, high);

        Instrument instrument = instrumentService.findAll()
                .stream() //
                .filter(inst -> inst.getActive()) //
                .findAny().get();
        final PreviousDayMarketRange
                priceRange = PreviousDayMarketRange.builder()
                .instrument(instrument)
                .date(Instant.now())
                .low(low)
                .high(high)
                .build();

        MarketStructure struct1Min = MarketStructure.builder().priceRange(priceRange)
                .modus(Modus.builder().mod(2).subtract(2).build())
                .objectMapper(objectMapper)
                .instrument(instrument)
                .marketDate(LocalDate.now())
                .marketSession(null)
                .intervals(2)
                .timeFrame(1)
                .timeFrameUnit(TimeFramesUnit.MINUTE)
                .build();

        struct1Min.init(candleList1Mins);

        MarketStructure struct5Mins = MarketStructure.builder().priceRange(priceRange)
                .modus(Modus.builder().mod(4).subtract(4).build())
                .objectMapper(objectMapper)
                .instrument(instrument)
                .marketDate(LocalDate.now())
                .childMarketStructure(struct1Min)
                .marketSession(null)
                .intervals(4)
                .timeFrame(5)
                .timeFrameUnit(TimeFramesUnit.MINUTE)
                .build();

        struct5Mins.init(candleList5Mins);

        MarketStructure struct15Mins = MarketStructure.builder().priceRange(priceRange)
                .modus(Modus.builder().mod(8).subtract(8).build())
                .objectMapper(objectMapper)
                .instrument(instrument)
                .marketDate(LocalDate.now())
                .marketSession(null)
                .intervals(8)
                .timeFrame(15)
                .childMarketStructure(struct5Mins)
                .timeFrameUnit(TimeFramesUnit.MINUTE)
                .build();
        struct15Mins.init(candleList15Mins);

        MarketStructure stuct30Mins = MarketStructure.builder().priceRange(priceRange)
                .modus(Modus.builder().mod(15).subtract(15).build())
                .objectMapper(objectMapper)
                .instrument(instrument)
                .marketDate(LocalDate.now())
                .marketSession(null)
                .intervals(15)
                .timeFrame(30)
                .childMarketStructure(struct15Mins)
                .timeFrameUnit(TimeFramesUnit.MINUTE)
                .build();

        stuct30Mins.init(candleList30Mins);

        MarketStructure struct1Hour = MarketStructure.builder().priceRange(priceRange)
                .modus(Modus.builder().mod(30).subtract(30).build())
                .objectMapper(objectMapper)
                .instrument(instrument)
                .marketDate(LocalDate.now())
                .marketSession(null)
                .timeFrame(1)
                .timeFrameUnit(TimeFramesUnit.HOUR)
                .childMarketStructure(stuct30Mins)
                .intervals(30)
                .build();

        struct1Hour.init(candleList1Hour);

        MarketStructure struct4Hour = MarketStructure.builder().priceRange(priceRange)
                .modus(Modus.builder().mod(40).subtract(40).build())
                .objectMapper(objectMapper)
                .instrument(instrument)
                .marketDate(LocalDate.now())
                .marketSession(null)
                .timeFrame(4)
                .timeFrameUnit(TimeFramesUnit.HOUR)
                .childMarketStructure(struct1Hour)
                .intervals(40)
                .build();

        struct4Hour.init(candleList4Hour);
        register(MarketTypes.GOLD_4_HOUR, struct4Hour);
    }


    public void initDefaultMarket(MarketStructure structure,
                                  final MarketTypes marketKey)
    {
        register(marketKey, structure);
    }

    public Map<MarketTypes, MarketStructure> getStructures()
    {
        return structures;
    }
}
