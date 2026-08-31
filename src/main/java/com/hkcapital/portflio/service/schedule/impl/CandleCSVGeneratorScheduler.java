package com.hkcapital.portflio.service.schedule.impl;

import com.hkcapital.portflio.model.Candle;
import com.hkcapital.portflio.model.Instrument;
import com.hkcapital.portflio.service.candle.etoro.EtoroCandleService;
import com.hkcapital.portflio.service.csv.impl.CandleCSVBuilder;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service("CandleCSVGenerator")
public class CandleCSVGeneratorScheduler implements ScheduleService
{
    private Logger logger = LoggerFactory.getLogger(CandleCSVGeneratorScheduler.class);
    private final EtoroCandleService candleService;
    private final EnvService envService;

    private final InstrumentService instrumentService;
    private static final DateTimeFormatter FILE_FORMAT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public CandleCSVGeneratorScheduler(final EtoroCandleService candleService,
                                       final EnvService envService,
                                       final InstrumentService instrumentService)
    {
        this.candleService = candleService;
        this.envService = envService;
        this.instrumentService = instrumentService;
    }

    //@Scheduled(cron = "0 5 23 * * MON-FRI")
    @Scheduled(cron = "0 5 22 * * MON-FRI")
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

        List<Instrument> instruments = instrumentService.findByActive(true);

        for (Instrument inst : instruments)
        {
            if (!inst.getWithCandle() || !inst.getWithBand())
            {
                continue;
            }

            List<Candle> candle = candleService.findByInstrumentIDAndCreationDateTimeBetween(inst.getEtoroInstrumentId(), start, end);

            String data = CandleCSVBuilder.buildCSV(candle);

            String pathProd = "D:/data/" + inst.getInstrumentTicker() + "/";
            String pathDev = "D:/data_dev/" + inst.getInstrumentTicker() + "/";
            String pathSim = "D:/data_sim/" + inst.getInstrumentTicker() + "/";

            LocalDate today = LocalDate.now();

            String folderName = pathProd + today.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + "/candle/";

            if (envService.getActiveProfile().equals("dev"))
            {
                folderName = pathDev + today.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + "/candle/";
            }

            if (envService.getActiveProfile().equals("simulation"))
            {
                folderName = pathSim + today.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + "/candle/";
            }

            String fileName = inst.getInstrumentTicker() + "_candle_" + today.format(FILE_FORMAT) + ".csv";
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



