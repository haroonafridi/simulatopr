package com.hkcapital.portflio.service.schedule.impl;

import com.hkcapital.portflio.market.structure.*;
import com.hkcapital.portflio.model.Instrument;
import com.hkcapital.portflio.service.export.ExtensionTypes;
import com.hkcapital.portflio.service.export.FileTypes;
import com.hkcapital.portflio.service.export.Literals;
import com.hkcapital.portflio.service.export.file.csv.CSVFileGenerator;
import com.hkcapital.portflio.service.instrument.InstrumentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service("MarketStructureCSVGenerator")
@Slf4j
public class MarketStructureCSVScheduler implements ScheduleService
{
    private static final String DATE_FORMAT = "yyyy-MM-dd_HH-mm-ss";
    private final MarketStructureCache marketStructureManagerCache;
    private InstrumentService instrumentService;
    private final CSVFileGenerator csvFileGenerator;
    private final String DASH = Literals.DASH.getValue();
    private final String FOLDER_NAME = "market-data/";
    public MarketStructureCSVScheduler(MarketStructureCache marketStructureManagerCache, //
                                       InstrumentService instrumentService,
                                       CSVFileGenerator csvFileGenerator)
    {
        this.marketStructureManagerCache = marketStructureManagerCache;
        this.instrumentService = instrumentService;
        this.csvFileGenerator = csvFileGenerator;
    }

    @Scheduled(cron = "0 0 4,8,11,15,23 * * MON-FRI")
    @Override
    public void run()
    {
        log.info("Generating csv file ");

        List<Instrument> bandInstruments = instrumentService.findByActiveAndWithBand(true, true);

        marketStructureManagerCache.getStructures().entrySet().forEach(str ->
        {
            for (Instrument inst : bandInstruments)
            {
                if (str.getValue().getInstrument().equals(inst))
                {
                    MarketStructure marketStructure = str.getValue();

                    PreviousDayMarketRangeDTO dayRange = //
                            new PreviousDayMarketRangeDTO(marketStructure.getPriceRange().getLow(),
                                    marketStructure.getPriceRange().getHigh());

                    MarketStructureDTO marketStructureDTO = MarketStructureDTO.from(marketStructure);

                    MarketStructureJsonWrapper marketStructureJsonWrapper =
                            MarketStructureJsonWrapper.builder()
                                    .marketStructure(marketStructureDTO)
                                    .creationDate(LocalDate.now())
                                    .previousDayRange(dayRange)//
                                    .build();
                    StringBuilder data = new StringBuilder("price_band");

                    data.append(",").append("band_type").append(",").append("lowerBound").append(",")
                            .append("upperBound").append(",").append("timeFrame").append(",")
                            .append("timeFrameUnit").append(",").append("initialVisitedTime").append(",")
                            .append("lastVisitedTime").append(",").append("marketVisitCount").append(",")
                            .append("timeDifference").append(",");

                    generateCSV(marketStructureJsonWrapper.getMarketStructure(), data);

                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);

                    String fileName = marketStructure.getInstrument().getInstrumentTicker() + //
                            DASH + FileTypes.MARKET_STRUCTURE.getType() +DASH + LocalDateTime.now().format(formatter) + ExtensionTypes.csv.getType();
                    csvFileGenerator.fileOf(data.toString(), FOLDER_NAME.concat(fileName), FileTypes.MARKET_STRUCTURE.getType());
                }
            }
        });
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
