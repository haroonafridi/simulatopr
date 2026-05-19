package com.hkcapital.portflio.market.structure;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hkcapital.portflio.model.Candle;
import com.hkcapital.portflio.model.Instrument;
import com.hkcapital.portflio.repository.registry.ServiceRegistery;
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
public class MarketStructureManagerCache implements Service
{
    private Logger logger = LoggerFactory.getLogger(MarketStructureManagerCache.class);
    private final Map<MarketTypes, MarketStructure> structures =
            new ConcurrentHashMap<>();
    private final EtoroCandleService candleService;
    private final InstrumentService instrumentService;

    private final ObjectMapper objectMapper;

    public MarketStructureManagerCache(final EtoroCandleService candleService,
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

        List<Candle> candleList = candleService.findByInstrumentIDAndTimeFrameAndTimeFrameUnitAndCreationDateTimeBetween(18, 15,
                "m", start, end);

        if(candleList.size() == 0)
        {
            logger.info("No candle data found cannot create market structure");
            return;
        }

        double low = candleList.stream().mapToDouble(c -> c.getLow()).min().getAsDouble();
        double high = candleList.stream().mapToDouble(c -> c.getHigh()).max().getAsDouble();
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

        MarketStructure structure = MarketStructure.builder().priceRange(priceRange)
                .modus(Modus.builder().mod(10).subtract(10).build())
                .objectMapper(objectMapper)
                .instrument(instrument)
                .marketSession(null)
                .intervals(10)
                .build();

        structure.init(candleList);

        register(MarketTypes.GOLD_15_MIN, structure);
    }

    public void initDefaultMarket(final Instrument instrument,
                                  final PriceRange priceRange,
                                  final Modus modus,
                                  final MarketSession marketSession,
                                  final Integer interval,
                                  final MarketTypes marketKey,
                                  final ObjectMapper objectMapper)
    {

        MarketStructure structure = MarketStructure.builder().priceRange(priceRange)
                .modus(Modus.builder()
                        .mod(modus.getMod())
                        .subtract(modus.getSubtract())
                        .build())
                .marketSession(marketSession)
                .objectMapper(objectMapper)
                .instrument(instrument)
                .intervals(interval)
                .build();
        register(marketKey, structure);
    }

}
