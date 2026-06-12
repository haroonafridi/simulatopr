package com.hkcapital.portflio.service.csv.impl;

import com.hkcapital.portflio.market.structure.DateTimeUtil;
import com.hkcapital.portflio.model.Candle;
import com.hkcapital.portflio.service.candle.etoro.EtoroCandleService;
import com.hkcapital.portflio.service.csv.CSVGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

public class CandleCSVGenerator implements CSVGenerator
{

    private final Logger logger = LoggerFactory.getLogger(CandleCSVGenerator.class);
    private final EtoroCandleService etoroCandleService;

    public CandleCSVGenerator(EtoroCandleService etoroCandleService)
    {
        this.etoroCandleService = etoroCandleService;
    }

    @Override
    public void generate(Instant start, Instant end)
    {

        LocalDateTime dateTime = DateTimeUtil.asLocalDateTime(start, ZoneOffset.UTC);
        LocalDateTime endTime = DateTimeUtil.asLocalDateTime(end, ZoneOffset.UTC);
        final String date = DateTimeUtil.toYearMonthDay(start);
        List<Candle> candle = etoroCandleService.findByInstrumentIDAndCreationDateTimeBetween(18,
                dateTime, endTime);
        String data = CandleCSVBuilder.buildCSV(candle);
        String folderName = "D:/gold_generated_data/" + date + "/candle/";
        String fileName = "gold_candle_" + date + ".csv";
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
            logger.info("Total csv ticks records created {} , Date from : {} , Date to: {} ", candle.size(), start, end);
        } catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }
}
