package com.hkcapital.portflio.market.structure;

import com.hkcapital.portflio.model.Candle;
import com.hkcapital.portflio.model.Instrument;
import com.hkcapital.portflio.service.candle.etoro.EtoroCandleService;
import com.hkcapital.portflio.service.instrument.InstrumentService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
public class MarketStructureManagerCache
{
    private Logger logger = LoggerFactory.getLogger(MarketStructureManagerCache.class);
    private final Map<String, MarketStructure> structures =
            new ConcurrentHashMap<>();

    private final EtoroCandleService candleService;

    private final InstrumentService instrumentService;

    public MarketStructureManagerCache(EtoroCandleService candleService,
                                       InstrumentService instrumentService)
    {
        this.candleService = candleService;
        this.instrumentService = instrumentService;
    }

    public void register(String key, MarketStructure structure)
    {
        structures.put(key, structure);
    }

    public MarketStructure get(String key)
    {
        return structures.get(key);
    }

    public void remove(String key)
    {
        structures.remove(key);
    }

    public boolean contains(String key)
    {
        return structures.containsKey(key);
    }

    @Scheduled(cron = "0 0 23 * * MON-FRI")
    public void closeMarket()
    {
        logger.info("Flushing cache");
        if (structures.get("15_mins_market") != null)
        {
            structures.get("15_mins_market").flush();
        }
    }

    @Scheduled(cron = "0 0 0 * * TUE-SAT")
    public void openMarket()
    {
        logger.info("Creating market cache..");

        LocalDateTime start = LocalDate.now()
                .minusDays(1)
                .atStartOfDay();

        LocalDateTime end = LocalDate.now()
                .atStartOfDay()
                .minusNanos(1);

        List<Candle> candleList = candleService.findByInstrumentIDAndTimeFrameAndTimeFrameUnitAndCreationDateTimeBetween(18, 15,
                "m", start, end);

        double low = candleList.stream().mapToDouble(c -> c.getLow()).min().getAsDouble();
        double high = candleList.stream().mapToDouble(c -> c.getHigh()).max().getAsDouble();

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
                .marketSession(null)
                .intervals(10)
                .build();

        structure.init(candleList);

        register("GOLD_15M", structure);
    }

}
