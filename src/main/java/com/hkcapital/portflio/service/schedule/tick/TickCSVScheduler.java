package com.hkcapital.portflio.service.schedule.tick;

import com.hkcapital.portflio.service.importexport.export.csv.feed.TickCSVGenerator;
import com.hkcapital.portflio.service.instrument.InstrumentService;
import com.hkcapital.portflio.service.schedule.ScheduleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import static com.hkcapital.portflio.util.DateTimeUtil.asOfDayEnd;
import static com.hkcapital.portflio.util.DateTimeUtil.asOfDayStart;

@Service("TickCSVGenerator")
@Slf4j
public class TickCSVScheduler implements ScheduleService
{
    private static final String SCHEDULE_TIME = "0 1 23 * * MON-FRI";
    private final InstrumentService instSrv;
    private final TickCSVGenerator tickCSVGenerator;

    public TickCSVScheduler(final InstrumentService instSrv,
                            final TickCSVGenerator tickCSVGenerator)
    {
        this.instSrv = instSrv;
        this.tickCSVGenerator = tickCSVGenerator;
    }

    @Scheduled(cron = SCHEDULE_TIME)
    @Override
    public void run()
    {
        log.info("Generating tick csv file ");
        instSrv.findByActiveAndWithFeed(true, true).forEach(el ->
        {
            tickCSVGenerator.generate(asOfDayStart(), asOfDayEnd(), el);
            log.info("Generating tick csv file generated successfully for instrument {} "
                    , el.getInstrumentTicker());
        });
    }
}



