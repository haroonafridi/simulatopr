package com.hkcapital.portoflio.service.schedule.impl;

import com.hkcapital.portoflio.broker.etoro.master.TimeFrame;
import com.hkcapital.portoflio.service.candle.etoro.EtoroCandleService;
import com.hkcapital.portoflio.service.instrument.InstrumentService;
import com.hkcapital.portoflio.service.schedule.ScheduleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service("OneDayDataScheduler")
public class OneDayDataScheduler implements ScheduleService
{
    private Logger logger = LoggerFactory.getLogger(FifteenMinutesDataScheduler.class);
    private final EtoroCandleService candleService;
    private final InstrumentService instrumentService;

    public OneDayDataScheduler(EtoroCandleService candleService, InstrumentService instrumentService)
    {
        this.candleService = candleService;
        this.instrumentService = instrumentService;
    }

    @Scheduled(cron = "0 0 0 * * *")
    @Override
    public void run()
    {
        logger.info("Running scheduler TimeFrame = 01 DAY");
        instrumentService.findByActive(Boolean.TRUE).forEach(instrument -> //
                candleService.fetchAndSaveCandleInformation(instrument.getEtoroInstrumentId(), //
                        TimeFrame.OneDay, 1));

    }
}
