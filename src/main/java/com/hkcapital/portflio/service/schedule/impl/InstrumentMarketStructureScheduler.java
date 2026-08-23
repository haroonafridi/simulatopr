package com.hkcapital.portflio.service.schedule.impl;

import com.hkcapital.portflio.market.structure.MarketPriceBand;
import com.hkcapital.portflio.market.structure.MarketStructure;
import com.hkcapital.portflio.market.structure.MarketStructureCache;
import com.hkcapital.portflio.market.structure.MarketTypes;
import com.hkcapital.portflio.model.Instrument;
import com.hkcapital.portflio.model.InstrumentMarketStructure;
import com.hkcapital.portflio.service.instrument.InstrumentService;
import com.hkcapital.portflio.service.marketstructure.InstrumentMarketStructureService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service("InstrumentMarketStructureGenerator")
public class InstrumentMarketStructureScheduler implements ScheduleService
{
    private Logger logger = LoggerFactory.getLogger(InstrumentMarketStructureScheduler.class);
    private final MarketStructureCache marketStructureManagerCache;
    private final InstrumentService instService;
    private final InstrumentMarketStructureService instMarkStrctrSrv;

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
        logger.info("Creating instrument bands in db");
        Instrument inst = instService.findByEtoroInstrumentId(18);
        MarketStructure hour4 = marketStructureManagerCache.get(MarketTypes.GOLD_4_HOUR);
        if (hour4 != null)
        {
            MarketStructure hour1 = hour4.getChildMarketStructure();
            MarketStructure mins30 = hour1.getChildMarketStructure();
            MarketStructure mins15 = mins30.getChildMarketStructure();
            MarketStructure mins5 = mins15.getChildMarketStructure();
            MarketStructure mins1 = mins5.getChildMarketStructure();
            hour4.getUpperBands().forEach(ms ->
            {
                createStructure(inst, ms);
            });
            hour4.getLowerBands().forEach(ms ->
            {
                createStructure(inst, ms);
            });
            hour1.getUpperBands().forEach(ms ->
            {
                createStructure(inst, ms);
            });
            hour1.getLowerBands().forEach(ms ->
            {
                createStructure(inst, ms);
            });
            mins30.getUpperBands().forEach(ms ->
            {
                createStructure(inst, ms);
            });
            mins30.getLowerBands().forEach(ms ->
            {
                createStructure(inst, ms);
            });
            mins15.getUpperBands().forEach(ms ->
            {
                createStructure(inst, ms);
            });

            mins15.getLowerBands().forEach(ms ->
            {
                createStructure(inst, ms);
            });


            mins5.getUpperBands().forEach(ms ->
            {
                createStructure(inst, ms);
            });

            mins5.getLowerBands().forEach(ms ->
            {
                createStructure(inst, ms);
            });

            mins1.getUpperBands().forEach(ms ->
            {
                createStructure(inst, ms);
            });

            mins1.getLowerBands().forEach(ms ->
            {
                createStructure(inst, ms);
            });
        }
    }

    private void createStructure(Instrument inst, MarketPriceBand ms)
    {
        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");

        String timestamp = LocalDateTime.now().format(formatter);

        InstrumentMarketStructure instrumentMarketStructure =
                InstrumentMarketStructure.builder()
                        .instrument(inst)
                        .bandKey(ms.getBandKey().toString())
                        .marketVisitCount(ms.getMarketVisitCount())
                        .initialVisitedTime(ms.getInitialVisitedTime())
                        .lastVisitedTime(ms.getLastVisitedTime())
                        .upperBound(ms.getUpperBound())
                        .lowerBound(ms.getLowerBound())
                        .timeDifference(ms.getTimeDifference())
                        .timeFrame(ms.getTimeFrame())
                        .timeFrameUnit(ms.getTimeFrameUnit().getUnit())
                        .bandType(ms.getBandType().getValue())
                        .marketStructureKey("generator_" + timestamp)
                        .creationDate(LocalDateTime.now())
                        .build();
        instMarkStrctrSrv.add(instrumentMarketStructure);
    }


}
