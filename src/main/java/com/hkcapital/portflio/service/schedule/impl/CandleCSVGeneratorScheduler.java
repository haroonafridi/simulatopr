package com.hkcapital.portflio.service.schedule.impl;

import com.hkcapital.portflio.model.Candle;
import com.hkcapital.portflio.service.candle.etoro.EtoroCandleService;
import com.hkcapital.portflio.service.csv.impl.CandleCSVBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service("CandleCSVGenerator")
public class CandleCSVGeneratorScheduler implements ScheduleService
{
    private Logger logger = LoggerFactory.getLogger(CandleCSVGeneratorScheduler.class);
    private final EtoroCandleService candleService;
    private static final DateTimeFormatter FILE_FORMAT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public CandleCSVGeneratorScheduler(final EtoroCandleService candleService)
    {
        this.candleService = candleService;
    }

    @Scheduled(cron = "0 5 23 * * MON-FRI")
    @Override
    public void run()
    {
        generateCandleCsvData();
    }

    private void generateCandleCsvData()
    {
        logger.info("Generating Candle csv file ");

        LocalDate targetDate = LocalDate.now();

        LocalDateTime start = targetDate.atStartOfDay();

        LocalDateTime end = targetDate
                .plusDays(1)
                .atStartOfDay()
                .minusNanos(1);

        List<Candle> candle = candleService.findByInstrumentIDAndCreationDateTimeBetween(18, start, end);

        String data = CandleCSVBuilder.buildCSV(candle);

        LocalDate today = LocalDate.now();

        String folderName = "D:/gold_data/"+today.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))+"/candle/";

        String fileName = "gold_candle_" + today.format(FILE_FORMAT) + ".csv";
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



