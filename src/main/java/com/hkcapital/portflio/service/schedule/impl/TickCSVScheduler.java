package com.hkcapital.portflio.service.schedule.impl;

import com.hkcapital.portflio.model.LiveInstrumentFeed;
import com.hkcapital.portflio.repository.liveinstrumentfeed.LiveInstrumentFeedRepository;
import com.hkcapital.portflio.service.csv.impl.LiveInstrumentFeedCsvGenerator;
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
    private static final DateTimeFormatter FILE_FORMAT =
            DateTimeFormatter.ofPattern("yyyy_MM_dd");

    public TickCSVScheduler(final LiveInstrumentFeedRepository liveInstrumentFeedRepository)
    {
        this.liveInstrumentFeedRepository = liveInstrumentFeedRepository;
    }

    @Scheduled(cron = "0 1 23 * * MON-FRI")
    @Override
    public void run()
    {
        logger.info("Generating ticker csv file ");
        ZoneId zone = ZoneId.systemDefault();
        final Instant fromDate = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0).atZone(zone).toInstant();
        final Instant toDate = LocalDateTime.now().withHour(23).withMinute(59).withSecond(59).withNano(0).atZone(zone).toInstant();
        final List<LiveInstrumentFeed> feed = liveInstrumentFeedRepository.findByFeedDateBetween(fromDate, toDate);
        String data = LiveInstrumentFeedCsvGenerator.generate(feed);
        LocalDate today = LocalDate.now();

        String folderName = "D:/gold_data/"+today.format(DateTimeFormatter.ofPattern("yyyy_MM_dd"))+"/tick/";

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



