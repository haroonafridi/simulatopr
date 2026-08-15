package com.hkcapital.portflio.service.schedule.impl;

import com.hkcapital.portflio.model.LiveInstrumentFeed;
import com.hkcapital.portflio.repository.liveinstrumentfeed.LiveInstrumentFeedRepository;
import com.hkcapital.portflio.service.csv.impl.LiveInstrumentFeedCsvGenerator;
import com.hkcapital.portflio.service.env.EnvService;
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
    private final EnvService envService;
    private static final DateTimeFormatter FILE_FORMAT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public TickCSVScheduler(final LiveInstrumentFeedRepository liveInstrumentFeedRepository,
                            EnvService envService)
    {
        this.liveInstrumentFeedRepository = liveInstrumentFeedRepository;
        this.envService = envService;
    }

    @Scheduled(cron = "0 1 23 * * MON-FRI")
    @Override
    public void run()
    {
        String pathProd ="D:/gold_data/";
        String pathDev ="D:/gold_data_dev/";
        String pathSim ="D:/gold_data_sim/";

        logger.info("Generating ticker csv file ");
        ZoneId zone = ZoneId.systemDefault();
        final Instant fromDate = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0).atZone(zone).toInstant();
        final Instant toDate = LocalDateTime.now().withHour(23).withMinute(59).withSecond(59).withNano(0).atZone(zone).toInstant();
        final List<LiveInstrumentFeed> feed = liveInstrumentFeedRepository.findByFeedDateBetween(fromDate, toDate);
        String data = LiveInstrumentFeedCsvGenerator.generate(feed);
        LocalDate today = LocalDate.now();

        String folderName = pathProd+today.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))+"/tick/";

        if(envService.getActiveProfile().equals("dev")) {
            folderName =  pathDev+today.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))+"/tick/";
        }

        if(envService.getActiveProfile().equals("simulation")) {
            folderName =  pathSim+today.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))+"/tick/";
        }

        String fileName = "gold_tick_" + today.format(FILE_FORMAT) + ".csv";
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



