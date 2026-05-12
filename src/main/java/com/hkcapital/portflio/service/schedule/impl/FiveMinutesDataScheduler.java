package com.hkcapital.portflio.service.schedule.impl;

import com.hkcapital.portflio.broker.etoro.master.TimeFrame;
import com.hkcapital.portflio.market.indicators.Unit;
import com.hkcapital.portflio.service.candle.etoro.EtoroCandleService;
import com.hkcapital.portflio.service.instrument.InstrumentService;
import com.hkcapital.portflio.service.schedule.ScheduleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


@Service("FiveMinutesDataScheduler")
public class FiveMinutesDataScheduler implements ScheduleService
{
    private Logger logger = LoggerFactory.getLogger(FifteenMinutesDataScheduler.class);
    private final EtoroCandleService etoroCandleService;
    private final InstrumentService instrumentService;

    public FiveMinutesDataScheduler(EtoroCandleService etoroCandleService, //
                                    InstrumentService instrumentService)
    {
        this.etoroCandleService = etoroCandleService;
        this.instrumentService = instrumentService;
    }

    //@Scheduled(cron = "0 */5 * * * *")
    @Override
    public void run()
    {
        logger.info("Running scheduler TimeFrame = 05 MINUTES");
        instrumentService.findByActive(Boolean.TRUE).forEach(instrument ->
                etoroCandleService.fetchAndSaveCandleInformation(instrument.getEtoroInstrumentId(),  //
                        TimeFrame.FiveMinutes, 1, Unit.MINUTE));

    }
}


