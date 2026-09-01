package com.hkcapital.portflio.market.structure;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hkcapital.portflio.market.indicators.TimeFramesUnit;
import com.hkcapital.portflio.model.Candle;
import com.hkcapital.portflio.model.Instrument;
import com.hkcapital.portflio.model.InstrumentMarketStructureConf;
import com.hkcapital.portflio.model.MarketConditions;
import com.hkcapital.portflio.service.candle.etoro.EtoroCandleService;
import com.hkcapital.portflio.service.instrument.InstrumentService;
import com.hkcapital.portflio.service.instrumentmarketstructureconf.InstrumentMarketStructureConfService;
import com.hkcapital.portflio.service.marketconditions.MarketConditionsService;
import com.hkcapital.portflio.service.registry.Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
public class MarketStructureCache implements Service
{
    private final Map<String, MarketStructure> structures =
            new ConcurrentHashMap<>();
    private final EtoroCandleService candleService;
    private final InstrumentService instrumentService;
    private final MarketConditionsService marketConditionsService;
    private final InstrumentMarketStructureConfService instMarkeStrConfSrv;

    private final ObjectMapper objectMapper;

    public MarketStructureCache(final EtoroCandleService candleService,
                                final InstrumentService instrumentService,
                                final MarketConditionsService marketConditionsService,
                                final InstrumentMarketStructureConfService instMarkeStrConfSrv,
                                final ObjectMapper objectMapper)
    {
        this.candleService = candleService;
        this.instrumentService = instrumentService;
        this.marketConditionsService = marketConditionsService;
        this.instMarkeStrConfSrv = instMarkeStrConfSrv;
        this.objectMapper = objectMapper;
    }

    public void register(String key, MarketStructure structure)
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
        log.info("Flushing cache");

        structures.entrySet().forEach(e ->
        {
            e.getValue().flush();
        });

    }

    @Scheduled(cron = "0 0 0 * * TUE-SAT")
    public void openMarket()
    {
        log.info("Creating market cache..");

        LocalDate today = LocalDate.now();

        DayOfWeek day = today.getDayOfWeek();

        LocalDate targetDate;

        if (day == DayOfWeek.MONDAY)
        {
            targetDate = today.minusDays(3); // Friday
        } else if (day == DayOfWeek.SUNDAY)
        {
            targetDate = today.minusDays(2); // Friday
        } else if (day == DayOfWeek.SATURDAY)
        {
            targetDate = today.minusDays(1); // Friday
        } else
        {
            targetDate = today.minusDays(1); // Yesterday
        }

        LocalDateTime start = targetDate.atStartOfDay();

        LocalDateTime end = targetDate
                .plusDays(1)
                .atStartOfDay()
                .minusNanos(1);

        List<Instrument> instruments = instrumentService.findByActive(true);

        for (Instrument inst : instruments)
        {
            if (inst.getWithBand() && inst.getWithCandle())
            {

                MarketStructure parentMarketStructure;

                List<InstrumentMarketStructureConf> instMrktConfs = instMarkeStrConfSrv.findByInstrumentAndActiveOrdeyByMarketOrder(inst, true);

                List<Candle> highLow = candleService.findByInstrumentIDAndTimeFrameAndTimeFrameUnitAndCreationDateTimeBetween(inst.getEtoroInstrumentId(),
                        15,
                        TimeFramesUnit.MINUTE.getUnit(), start, end);

                double low = 0;
                double high = 0;

                if (highLow.size() == 0)
                {
                    MarketConditions marketConditions = marketConditionsService.findByInstrumentOrderByIdDesc(inst);
                    if (marketConditions != null)
                    {
                        low = marketConditions.getDayLow();
                        high = marketConditions.getDayHigh();
                    }
                } else
                {
                    low = highLow.stream().mapToDouble(c -> c.getLow()).min().getAsDouble();
                    high = highLow.stream().mapToDouble(c -> c.getHigh()).max().getAsDouble();
                }

                MarketStructure childMarketStructure = null;
                InstrumentMarketStructureConf parentConf = null;
                for (InstrumentMarketStructureConf instMrktConf : instMrktConfs)
                {
                    List<Candle> candleList = candleService.findByInstrumentIDAndTimeFrameAndTimeFrameUnitAndCreationDateTimeBetween(inst.getEtoroInstrumentId(),
                            instMrktConf.getTimeFrame(),
                            instMrktConf.getTimeFrameUnit(), start, end);

                    final PreviousDayMarketRange
                            priceRangeGold = PreviousDayMarketRange.builder()
                            .instrument(inst)
                            .date(Instant.now())
                            .low(low)
                            .high(high)
                            .build();

                    MarketStructure marketStructure = MarketStructure.builder().priceRange(priceRangeGold)
                            .modus(Modus.builder().mod(instMrktConf.getModule()).subtract(instMrktConf.getSub()).build())
                            .objectMapper(objectMapper)
                            .instrument(inst)
                            .marketDate(LocalDate.now())
                            .childMarketStructure(childMarketStructure)
                            .marketSession(null)
                            .intervals(instMrktConf.getIntrvl())
                            .timeFrame(instMrktConf.getTimeFrame())
                            .timeFrameUnit(TimeFramesUnit.valueOf(instMrktConf.getTimeFrameUnit()))
                            .build();
                    marketStructure.init(candleList);
                    childMarketStructure = marketStructure;
                    parentConf = instMrktConf;
                }
                parentMarketStructure = childMarketStructure;
                if (parentConf != null)
                {
                    register(parentConf.getStructureName(), parentMarketStructure);
                }

            } else
            {
                log.info("Candle and Market bands will not be generarted for instrumentTicker [{}] ", inst.getInstrumentTicker());
            }
        }
    }


    public void initDefaultMarket(MarketStructure structure,
                                  final String marketKey)
    {
        register(marketKey, structure);
    }

    public Map<String, MarketStructure> getStructures()
    {
        return structures;
    }
}
