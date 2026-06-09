package com.hkcapital.portflio.market.structure.it;

import com.hkcapital.portflio.market.indicators.TimeFramesUnit;
import com.hkcapital.portflio.market.structure.MarketSession;
import com.hkcapital.portflio.market.structure.MarketStructure;
import com.hkcapital.portflio.market.structure.Modus;
import com.hkcapital.portflio.market.structure.PreviousDayMarketRange;
import com.hkcapital.portflio.model.Candle;
import com.hkcapital.portflio.model.Instrument;
import com.hkcapital.portflio.service.candle.etoro.EtoroCandleService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

@SpringBootTest
class MarketStructureFlushEtoro_IT
{

    @Autowired
    private EtoroCandleService candleService;

    @Test
    void shouldFlushMarketStructureForTheCurrentDay15MinTimeFrame() throws IOException
    {
        candleService.removeAll();
        LocalDateTime may112026_00_hr = LocalDateTime.of(2026, 05, 11, 00, 00, 01);
        LocalDateTime may112026_23_hr = LocalDateTime.of(2026, 05, 11, 23, 59, 59);

        List<String> candlesLine = Files.readAllLines(Path.of("D:/gold_data/gold_candle_11_05_2026.csv"));

        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS");
        
        for (String line:candlesLine)
        {
            String data[] = line.split(",");
            double high  = Double.parseDouble(data[0]);
            double low  =  Double.parseDouble(data[1]);
            LocalDateTime dateTime = LocalDateTime.parse(data[2], formatter);
            Candle candle = Candle.builder().instrumentID(18)
                    .high(high)
                    .low(low)
                    .timeFrame(15)
                    .creationDateTime(dateTime)
                    .timeFrameUnit("m").build();
            candleService.save(candle);
        }

        List<Candle> candlesList = candleService
                .findByInstrumentIDAndTimeFrameAndTimeFrameUnitAndCreationDateTimeBetween(18,15,
                        TimeFramesUnit.MINUTE.getUnit(), may112026_00_hr, may112026_23_hr);

        MarketSession marketSession = MarketSession.builder()
                .name("ASIA/TOKYO").start(Instant.now())
                .end(Instant.now().plus(8, ChronoUnit.HOURS))
                .build();

        final String instName = "GOLD";
        final String instTicker = "XAU/USD";
        final String instDesc = "Gold Standard CFD";
        final double maxSlippage = 1.75;
        final boolean active = true;
        final Integer etoroInstId = 18;
        final Instant date = Instant.parse("2026-05-11T23:59:59.00Z");
        final double low = 4646.96;
        final double high = 4749.02;
        final Instrument instrument = Instrument.builder()
                .name(instName)
                .instrumentTicker(instTicker)
                .instrumentDesc(instDesc)
                .maxSlippage(maxSlippage)
                .active(active)
                .etoroInstrumentId(etoroInstId)
                .build();
        final PreviousDayMarketRange
                range = PreviousDayMarketRange.builder()
                .instrument(instrument)
                .date(date)
                .low(low)
                .high(high)
                .build();
        MarketStructure marketStructure = //
                MarketStructure
                        .builder()
                        .intervals(10)
                        .marketSession(marketSession)
                        .modus(Modus.builder().mod(10)
                                .subtract(10).build())
                        .marketDate(LocalDate.now())
                        .priceRange(range)
                        .build();
        marketStructure.init(candlesList);
        marketStructure.flush();
        Assertions.assertEquals(92, candlesList.size());
        Assertions.assertEquals(0, marketStructure.getUpperBands().size());
        Assertions.assertEquals(0, marketStructure.getLowerBands().size());

    }
}