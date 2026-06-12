package com.hkcapital.portflio.service.schedule.impl;

import com.hkcapital.portflio.broker.etoro.master.TimeFrame;
import com.hkcapital.portflio.market.indicators.TimeFramesUnit;
import com.hkcapital.portflio.service.candle.etoro.EtoroCandleService;
import com.hkcapital.portflio.service.instrument.InstrumentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


@Service("OneWeekDataScheduler")
public class OneWeekDataScheduler implements ScheduleService
{
    private Logger logger = LoggerFactory.getLogger(FifteenMinutesDataScheduler.class);
    private final EtoroCandleService candleService;
    private final InstrumentService instrumentService;

    public OneWeekDataScheduler(EtoroCandleService candleService, //
                                InstrumentService instrumentService)
    {
        this.candleService = candleService;
        this.instrumentService = instrumentService;
    }

    //@Scheduled(cron = "0 0 0 * * MON")
    @Override
    public void run()
    {
        logger.info("Running scheduler TimeFrame = 01 WEEK");
        instrumentService.findByActive(Boolean.TRUE).forEach(instrument -> //
                candleService.fetchAndSaveCandleInformation(instrument.getEtoroInstrumentId(), //
                        TimeFrame.OneWeek, 1, TimeFramesUnit.MINUTE));
    }
}
