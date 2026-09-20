package com.hkcapital.portflio.service.importexport.export.csv.marketstructure;

import com.hkcapital.portflio.market.structure.*;
import com.hkcapital.portflio.model.Instrument;
import com.hkcapital.portflio.model.LiveInstrumentFeed;
import com.hkcapital.portflio.service.importexport.ExtensionTypes;
import com.hkcapital.portflio.service.importexport.FileTypes;
import com.hkcapital.portflio.service.importexport.Literals;
import com.hkcapital.portflio.service.importexport.export.csv.CSVGenerator;
import com.hkcapital.portflio.service.importexport.export.csv.feed.LiveInstrumentFeedCsvGenerator;
import com.hkcapital.portflio.service.importexport.export.file.csv.CSVFileGenerator;
import com.hkcapital.portflio.service.instrument.InstrumentService;
import com.hkcapital.portflio.service.marketfeed.LiveInstrumentFeedService;
import com.hkcapital.portflio.util.DateTimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static com.hkcapital.portflio.service.importexport.GenerateParameterValidator.validateGenerateParameters;
import static com.hkcapital.portflio.util.DateTimeUtil.asLocalDateTime;

@Service
@Slf4j
public class MarketStructureCSVGenerator implements CSVGenerator
{
    private static final String DATE_FORMAT = "yyyy-MM-dd_HH-mm-ss";
    private final MarketStructureCache marketStructureManagerCache;
    private com.hkcapital.portflio.service.instrument.InstrumentService instrumentService;
    private final CSVFileGenerator csvFileGenerator;
    private final String DASH = Literals.DASH.getValue();
    private final String FOLDER_NAME = "market-data/";

    public MarketStructureCSVGenerator(final MarketStructureCache marketStructureManagerCache, //
                                       final InstrumentService instrumentService,
                                       final CSVFileGenerator csvFileGenerator)
    {
        this.marketStructureManagerCache = marketStructureManagerCache;
        this.instrumentService = instrumentService;
        this.csvFileGenerator = csvFileGenerator;
    }
    @Override
    public int generate(final Instant fromDate, final Instant toDate, final Instrument instrument)
    {
        log.info("Generating csv file for market structure ");

        List<Instrument> bandInstruments = //
                instrumentService.findByActiveAndWithBand(true, true);

        marketStructureManagerCache.getStructures().entrySet().forEach(structure ->
        {
            for (Instrument inst : bandInstruments)
            {
                if (structure.getValue().getInstrument().equals(inst))
                {
                    final MarketStructure marketStructure = structure.getValue();

                    final PreviousDayMarketRangeDTO dayRange = //
                            new PreviousDayMarketRangeDTO(marketStructure.getPriceRange().getLow(),
                                    marketStructure.getPriceRange().getHigh());

                    final MarketStructureDTO marketStructureDTO = MarketStructureDTO.from(marketStructure);

                    final MarketStructureJsonWrapper marketStructureJsonWrapper =
                            MarketStructureJsonWrapper.builder()
                                    .marketStructure(marketStructureDTO)
                                    .creationDate(LocalDate.now())
                                    .previousDayRange(dayRange)//
                                    .build();

                    final StringBuilder data = new StringBuilder("price_band");

                    appendData(data);

                    generateCSV(marketStructureJsonWrapper.getMarketStructure(), data);

                    final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);

                    final String fileName = marketStructure.getInstrument().getInstrumentTicker() + //
                            DASH + FileTypes.MARKET_STRUCTURE.getType() + DASH + LocalDateTime.now().format(formatter) + ExtensionTypes.csv.getType();
                    csvFileGenerator.fileOf(data.toString(), FOLDER_NAME.concat(fileName), FileTypes.MARKET_STRUCTURE);
                }
            }
        });
        return -1;
    }

    private static void appendData(StringBuilder data)
    {
        data.append(",")
                .append("band_type")
                .append(",")
                .append("lowerBound")
                .append(",")
                .append("upperBound")
                .append(",")
                .append("timeFrame")
                .append(",")
                .append("timeFrameUnit")
                .append(",")
                .append("initialVisitedTime")
                .append(",")
                .append("lastVisitedTime")
                .append(",")
                .append("marketVisitCount")
                .append(",")
                .append("timeDifference")
                .append(",");
    }

    private StringBuilder generateCSV(final MarketStructureDTO marketStructure,
                                      final StringBuilder csv)
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

    private String safe(final Object value)
    {
        return value == null ? "" : value.toString();
    }
}







