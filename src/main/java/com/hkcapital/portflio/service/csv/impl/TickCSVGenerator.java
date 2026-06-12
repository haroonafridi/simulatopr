package com.hkcapital.portflio.service.csv.impl;

import com.hkcapital.portflio.market.structure.DateTimeUtil;
import com.hkcapital.portflio.model.LiveInstrumentFeed;
import com.hkcapital.portflio.service.csv.CSVGenerator;
import com.hkcapital.portflio.service.marketfeed.LiveInstrumentFeedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class TickCSVGenerator implements CSVGenerator
{
    private final Logger logger = LoggerFactory.getLogger(TickCSVGenerator.class);
    private final LiveInstrumentFeedService liveInstrumentFeedService;

    public TickCSVGenerator(LiveInstrumentFeedService liveInstrumentFeedService)
    {
        this.liveInstrumentFeedService = liveInstrumentFeedService;
    }

    @Override
    public void generate(Instant fromDate, Instant toDate)
    {

        final String date =  DateTimeUtil.toYearMonthDay(fromDate);

        final List<LiveInstrumentFeed> feed = liveInstrumentFeedService.findByFeedDateBetween(fromDate, toDate);

        String folderName = "D:/gold_generated_data/" + date + "/tick/";

        String fileName = "gold_tick_" + date + ".csv";
        try
        {
            Path folder = Path.of(folderName);
            Files.createDirectories(folder);
            Path csvFile = folder.resolve(fileName);
            Files.writeString(
                    csvFile,
                    LiveInstrumentFeedCsvGenerator.generate(feed),
                    StandardCharsets.UTF_8
            );
            logger.info("Total csv ticks records created {} , Date from : {} , Date to: {} ", feed.size(), fromDate, toDate);
        } catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }
}
