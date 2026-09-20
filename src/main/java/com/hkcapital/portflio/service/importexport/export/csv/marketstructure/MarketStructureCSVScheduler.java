package com.hkcapital.portflio.service.importexport.export.csv.marketstructure;

import com.hkcapital.portflio.service.instrument.InstrumentService;
import com.hkcapital.portflio.service.schedule.ScheduleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service("MarketStructureCSVGenerator")
@Slf4j
public class MarketStructureCSVScheduler implements ScheduleService
{
    private final MarketStructureCSVGenerator marketStructureCSVGenerator;
    private final InstrumentService instrumentService;

    public MarketStructureCSVScheduler(MarketStructureCSVGenerator marketStructureCSVGenerator,
                                       InstrumentService instrumentService)
    {
        this.marketStructureCSVGenerator = marketStructureCSVGenerator;
        this.instrumentService = instrumentService;
    }

    @Scheduled(cron = "0 0 4,8,11,15,23 * * MON-FRI")
    @Override
    public void run()
    {
        marketStructureCSVGenerator.generate(null, null, null);
    }
}
