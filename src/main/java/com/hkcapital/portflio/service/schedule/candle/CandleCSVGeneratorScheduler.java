package com.hkcapital.portflio.service.schedule.candle;

import com.hkcapital.portflio.service.importexport.export.csv.candle.CandleCSVGenerator;
import com.hkcapital.portflio.service.instrument.InstrumentService;
import com.hkcapital.portflio.service.schedule.ScheduleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import static com.hkcapital.portflio.util.DateTimeUtil.asOfDayEnd;
import static com.hkcapital.portflio.util.DateTimeUtil.asOfDayStart;
@Service("CandleCSVGenerator")
@Slf4j
public class CandleCSVGeneratorScheduler implements ScheduleService
{
    private static final String CRON = "0 05 23 * * MON-FRI";
    private final CandleCSVGenerator candleCSVGenerator;
    private final InstrumentService instrumentService;
    public CandleCSVGeneratorScheduler(final InstrumentService instrumentService,
                                       final CandleCSVGenerator candleCSVGenerator)
    {
        this.instrumentService = instrumentService;
        this.candleCSVGenerator = candleCSVGenerator;
    }
    @Scheduled(cron = CRON)
    @Override
    public void run()
    {
        log.info("Generating Candle daily csv file started. ");
        instrumentService //
                .findByActiveAndWithCandle(true, true) //
                .forEach(el ->
                {
                    candleCSVGenerator.generate(asOfDayStart(), asOfDayEnd(), el);
                });
    }

}



