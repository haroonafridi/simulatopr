package com.hkcapital.portflio.service.schedule.impl;

import com.hkcapital.portflio.market.structure.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service("MarketStructureCSVGenerator")
public class MarketStructureCSVScheduler implements ScheduleService
{
    private Logger logger = LoggerFactory.getLogger(MarketStructureCSVScheduler.class);
    private final MarketStructureCache marketStructureManagerCache;
    public MarketStructureCSVScheduler(MarketStructureCache marketStructureManagerCache)
    {
        this.marketStructureManagerCache = marketStructureManagerCache;
    }

    @Scheduled(cron = "0 0 4,8,11,15,23 * * MON-FRI")
    @Override
    public void run()
    {
        logger.info("Generating csv file ");
        extractAndGenerateFile();

    }


    private void extractAndGenerateFile()
    {
        MarketStructure hour4 = marketStructureManagerCache.get(MarketTypes.GOLD_4_HOUR);
        MarketStructure hour1 = hour4.getChildMarketStructure();
        MarketStructure mins30 = hour1.getChildMarketStructure();
        MarketStructure mins15 = mins30.getChildMarketStructure();
        MarketStructure mins5 = mins15.getChildMarketStructure();
        MarketStructure mins1 = mins5.getChildMarketStructure();
        MarketStructureDTO mins1Dto = MarketStructureDTO.builder().marketDate(mins1.getMarketDate())
                .timeFrameUnit(mins1.getTimeFrameUnit())
                .timeFrame(mins1.getTimeFrame())
                .lowerBands(mins1.getLowerBands())
                .upperBands(mins1.getUpperBands())
                .build();
        MarketStructureDTO mins5Dto = MarketStructureDTO.builder().marketDate(mins5.getMarketDate())
                .timeFrameUnit(mins5.getTimeFrameUnit())
                .timeFrame(mins5.getTimeFrame())
                .lowerBands(mins5.getLowerBands())
                .upperBands(mins5.getUpperBands())
                .child(mins1Dto)
                .build();
        MarketStructureDTO mins15Dto = MarketStructureDTO.builder().marketDate(mins15.getMarketDate())
                .timeFrameUnit(mins15.getTimeFrameUnit())
                .timeFrame(mins15.getTimeFrame())
                .lowerBands(mins15.getLowerBands())
                .upperBands(mins15.getUpperBands())
                .child(mins5Dto)
                .build();
        MarketStructureDTO mins30Dto = MarketStructureDTO.builder().marketDate(mins30.getMarketDate())
                .timeFrameUnit(mins30.getTimeFrameUnit())
                .timeFrame(mins30.getTimeFrame())
                .lowerBands(mins30.getLowerBands())
                .upperBands(mins30.getUpperBands())
                .child(mins15Dto)
                .build();
        MarketStructureDTO hour1Dto = MarketStructureDTO.builder().marketDate(hour1.getMarketDate())
                .timeFrameUnit(hour1.getTimeFrameUnit())
                .timeFrame(hour1.getTimeFrame())
                .lowerBands(hour1.getLowerBands())
                .upperBands(hour1.getUpperBands())
                .child(mins30Dto)
                .build();

        MarketStructureDTO hour4Dto = MarketStructureDTO.builder().marketDate(hour4.getMarketDate())
                .timeFrameUnit(hour4.getTimeFrameUnit())
                .timeFrame(hour4.getTimeFrame())
                .lowerBands(hour4.getLowerBands())
                .upperBands(hour4.getUpperBands())
                .child(hour1Dto)
                .build();

        PreviousDayMarketRangeDTO dayRange = //
                new PreviousDayMarketRangeDTO(hour4.getPriceRange().getLow(), hour4.getPriceRange().getHigh());
        MarketStructureJsonWrapper marketStructureJsonWrapper = MarketStructureJsonWrapper.builder()
                .marketStructure(hour4Dto)
                .creationDate(LocalDate.now()).previousDayRange(dayRange).build();
        StringBuilder data = new StringBuilder("price_band");
        data.append(",").append("band_type").append(",").append("lowerBound").append(",")
                .append("upperBound").append(",").append("timeFrame").append(",")
                .append("timeFrameUnit").append(",").append("initialVisitedTime").append(",")
                .append("lastVisitedTime").append(",").append("marketVisitCount").append(",")
                .append("timeDifference").append(",");

        generateCSV(marketStructureJsonWrapper.getMarketStructure(), data);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");

        Path outputFile = Path.of(
                "market-data",
                "gold-market-structure_" + LocalDateTime.now().format(formatter) + ".csv"
        );

        try
        {
            Files.createDirectories(outputFile.getParent());
            Files.writeString(outputFile, data.toString());
        } catch (IOException e)
        {
            logger.info("Cannot write csv file to location {}");
            throw new RuntimeException(e);
        }
    }


    private StringBuilder generateCSV(MarketStructureDTO marketStructure, StringBuilder csv)
    {
        if (marketStructure == null)
        {
            return csv;
        }
        appendBands(csv, marketStructure.getUpperBands().stream().toList(), "UPPER");
        appendBands(csv, marketStructure.getLowerBands().stream().toList(), "LOWER");
        return generateCSV(marketStructure.getChild(), csv);
    }

    private void appendBands(StringBuilder csv,
                             List<MarketPriceBand> bands,
                             String bandType)
    {
        if (bands == null)
        {
            return;
        }

        for (MarketPriceBand band : bands)
        {
            csv.append("\n")
                    .append(safe(band.getBandKey())).append(",")
                    .append(bandType).append(",")
                    .append(safe(band.getLowerBound())).append(",")
                    .append(safe(band.getUpperBound())).append(",")
                    .append(safe(band.getTimeFrame())).append(",")
                    .append(safe(band.getTimeFrameUnit())).append(",")
                    .append(safe(band.getInitialVisitedTime())).append(",")
                    .append(safe(band.getLastVisitedTime())).append(",")
                    .append(safe(band.getMarketVisitCount())).append(",")
                    .append(safe(band.getTimeDifference()));
        }
    }

    private String safe(Object value)
    {
        return value == null ? "" : value.toString();
    }
}
