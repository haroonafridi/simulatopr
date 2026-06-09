package com.hkcapital.portflio.market.structure.ut;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.hkcapital.portflio.market.structure.MarketPriceBand;
import com.hkcapital.portflio.market.structure.MarketStructure;
import com.hkcapital.portflio.market.structure.MarketStructureDTO;
import com.hkcapital.portflio.market.structure.MarketStructureJsonWrapper;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class MarketStructureJsonWrapperTest
{
    protected ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();
    @Test
    public void shouldLoadAndCreateMarketStructure() throws IOException
    {
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        MarketStructureJsonWrapper marketStructureJsonWrapper =
                objectMapper.readValue(new String(getClass().getResourceAsStream("/data/marketstructure/gold_marketstructure_09-06-2026.json").readAllBytes(), //
                        StandardCharsets.UTF_8),
                MarketStructureJsonWrapper.class);
        StringBuilder data =  new StringBuilder("price_band");
        data.append(",").append("band_type").append(",").append("lowerBound").append(",")
                .append("upperBound").append(",").append("timeFrame").append(",")
                        .append("timeFrameUnit").append(",").append("initialVisitedTime").append(",")
                        .append("lastVisitedTime").append(",").append("marketVisitCount").append(",")
                        .append("timeDifference").append(",");

        generateCSV(marketStructureJsonWrapper.getMarketStructure(), data);

        Path outputFile = Path.of(
                "target",
                "market-structure-" + marketStructureJsonWrapper.getMarketDate() + ".csv");

        Files.createDirectories(outputFile.getParent());
        Files.writeString(outputFile, data.toString());
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
