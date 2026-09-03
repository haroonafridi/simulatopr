package com.hkcapital.portflio.service.schedule.impl;

import com.hkcapital.portflio.market.structure.MarketStructure;
import com.hkcapital.portflio.market.structure.MarketStructureCache;
import com.hkcapital.portflio.model.Instrument;
import com.hkcapital.portflio.model.InstrumentMarketStructure;
import com.hkcapital.portflio.service.instrument.InstrumentService;
import com.hkcapital.portflio.service.marketstructure.InstrumentMarketStructureService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service("InstrumentMarketStructureGenerator")
@Slf4j
public class InstrumentMarketStructureScheduler implements ScheduleService
{
    private final MarketStructureCache marketStructureManagerCache;
    private final InstrumentService instService;
    private final InstrumentMarketStructureService instMarkStrctrSrv;

    private static final DateTimeFormatter TIMESTAMP_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");

    public InstrumentMarketStructureScheduler(MarketStructureCache marketStructureManagerCache,
                                              InstrumentService instService,
                                              InstrumentMarketStructureService instMarkStrctrSrv)
    {
        this.marketStructureManagerCache = marketStructureManagerCache;
        this.instService = instService;
        this.instMarkStrctrSrv = instMarkStrctrSrv;
    }

    @Scheduled(cron = "0 5 23 * * MON-FRI")
    @Override
    public void run()
    {
        extractAndCreateBands();

    }

    private void extractAndCreateBands()
    {
        log.info("Creating instrumentTicker bands in db");

        List<Instrument> insts = instService.findByActiveAndWithBand(true, true);

        for (Instrument inst : insts)
        {
            marketStructureManagerCache.getStructures().entrySet().forEach(el ->
            {
                createStructure(inst, el.getValue());
            });
        }
    }

    private void createStructure(Instrument inst, MarketStructure el)
    {

        if (el != null) //
        {
            final String timestamp = LocalDateTime.now().format(TIMESTAMP_FORMATTER);
            el.getLowerBands().forEach(e ->
            {
                InstrumentMarketStructure instrumentMarketStructure =
                        InstrumentMarketStructure.builder()
                                .instrument(inst)
                                .bandKey(e.getBandKey().toString())
                                .marketVisitCount(e.getMarketVisitCount())
                                .initialVisitedTime(e.getInitialVisitedTime())
                                .lastVisitedTime(e.getLastVisitedTime())
                                .upperBound(e.getUpperBound())
                                .lowerBound(e.getLowerBound())
                                .timeDifference(e.getTimeDifference())
                                .timeFrame(e.getTimeFrame())
                                .timeFrameUnit(e.getTimeFrameUnit().getUnit())
                                .bandType(e.getBandType().getValue())
                                .instrument(e.getInstrument())
                                .marketStructureKey("generator_" + timestamp)
                                .creationDate(LocalDateTime.now())
                                .build();
                instMarkStrctrSrv.add(instrumentMarketStructure);
            });

            el.getUpperBands().forEach(e ->
            {
                InstrumentMarketStructure instrumentMarketStructure =
                        InstrumentMarketStructure.builder()
                                .instrument(inst)
                                .bandKey(e.getBandKey().toString())
                                .marketVisitCount(e.getMarketVisitCount())
                                .initialVisitedTime(e.getInitialVisitedTime())
                                .lastVisitedTime(e.getLastVisitedTime())
                                .upperBound(e.getUpperBound())
                                .lowerBound(e.getLowerBound())
                                .timeDifference(e.getTimeDifference())
                                .timeFrame(e.getTimeFrame())
                                .timeFrameUnit(e.getTimeFrameUnit().getUnit())
                                .bandType(e.getBandType().getValue())
                                .instrument(e.getInstrument())
                                .marketStructureKey("generator_" + timestamp)
                                .creationDate(LocalDateTime.now())
                                .build();
                instMarkStrctrSrv.add(instrumentMarketStructure);
            });

            createStructure(inst, el.getChildMarketStructure());
        }
    }
}
