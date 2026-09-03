package com.hkcapital.portflio.service.schedule.impl;

import com.hkcapital.portflio.model.Instrument;
import com.hkcapital.portflio.model.LiveInstrumentFeed;
import com.hkcapital.portflio.repository.liveinstrumentfeed.LiveInstrumentFeedRepository;
import com.hkcapital.portflio.service.csv.impl.LiveInstrumentFeedCsvGenerator;
import com.hkcapital.portflio.service.env.EnvService;
import com.hkcapital.portflio.service.instrument.InstrumentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service("TickCSVGenerator")
public class TickCSVScheduler implements ScheduleService
{
    private Logger logger = LoggerFactory.getLogger(TickCSVScheduler.class);
    private final LiveInstrumentFeedRepository liveInstrumentFeedRepository;
    private final InstrumentService instrumentService;
    private final EnvService envService;
    private static final DateTimeFormatter FILE_FORMAT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public TickCSVScheduler(LiveInstrumentFeedRepository liveInstrumentFeedRepository, InstrumentService instrumentService, EnvService envService)
    {
        this.liveInstrumentFeedRepository = liveInstrumentFeedRepository;
        this.instrumentService = instrumentService;
        this.envService = envService;
    }

    @Scheduled(cron = "0 1 23 * * MON-FRI")
    @Override
    public void run()
    {
        String pathProd = "D:/tick_data_prod/";
        String pathDev = "D:/tick_data_dev/";
        String pathSim = "D:/tick_data_sim/";

        logger.info("Generating ticker csv file ");
        ZoneId zone = ZoneId.systemDefault();
        final Instant fromDate = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0).atZone(zone).toInstant();
        final Instant toDate = LocalDateTime.now().withHour(23).withMinute(59).withSecond(59).withNano(0).atZone(zone).toInstant();

        List<Instrument> instruments = instrumentService.findByActiveAndWithFeed(true, true);

        for (Instrument inst : instruments)
        {
            final List<LiveInstrumentFeed> feed = liveInstrumentFeedRepository //
                    .findByInstrumentIdAndFeedDateBetween(inst.getEtoroInstrumentId(),
                            fromDate, toDate);

            String data = LiveInstrumentFeedCsvGenerator.generate(feed);
            LocalDate today = LocalDate.now();

            String folderName = pathProd + today.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + "/tick/";

            if (envService.getActiveProfile().equals("dev"))
            {
                folderName = pathDev + today.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + "/tick/";
            }

            if (envService.getActiveProfile().equals("simulation"))
            {
                folderName = pathSim + today.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + "/tick/";
            }

            String fileName = inst.getInstrumentTicker() + "_tick_" + today.format(FILE_FORMAT) + ".csv";
            try
            {
                Path folder = Path.of(folderName);
                Files.createDirectories(folder);
                Path csvFile = folder.resolve(fileName);
                Files.writeString(
                        csvFile,
                        data,
                        StandardCharsets.UTF_8
                );
            } catch (IOException e)
            {
                throw new RuntimeException(e);
            }
        }
    }

}



