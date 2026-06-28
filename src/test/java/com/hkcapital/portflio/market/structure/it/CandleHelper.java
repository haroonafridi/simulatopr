package com.hkcapital.portflio.market.structure.it;

import com.hkcapital.portflio.market.structure.PreviousDayMarketRange;
import com.hkcapital.portflio.model.Candle;
import com.hkcapital.portflio.model.Instrument;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CandleHelper
{
    private static List<Candle> candles;

    public CandleHelper(Path path) {
        try
        {
            candles = loadPreviousDayCandle(path);
        } catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    public PreviousDayMarketRange getPreviousDayMarketRange() {
        Instrument gold = Instrument.builder()
                .etoroInstrumentId(18)
                .build();
        double low = candles.stream().mapToDouble(c->c.getLow()).min().getAsDouble();
        double high = candles.stream().mapToDouble(c->c.getHigh()).max().getAsDouble();
        return PreviousDayMarketRange.builder()
                .instrument(gold)
                .date(Instant.now())
                .low(low)
                .high(high)
                .build();
    }

    public List<Candle> candleListOf(Integer timeFrame, String timeFrameUnit)
    {
        return candles.stream().filter(c -> c.getTimeFrame().intValue() == timeFrame.intValue() &&
                c.getTimeFrameUnit().equals(timeFrameUnit)).collect(Collectors.toList());
    }

    private  List<Candle> loadPreviousDayCandle(Path path) throws IOException //
    {
        List<Candle> candles = new ArrayList<>();
        List<String> lines = Files.readAllLines(path);

        int count = 0;
        for (String line : lines)
        {
            if (count > 0)
            {
                String[] elements = line.split(",");
                Integer instrumentID = Integer.parseInt(elements[1]);
                Instant fromDate = Instant.parse(elements[2]);
                double open = Double.parseDouble(elements[3]);
                double high = Double.parseDouble(elements[4]);
                Instant highTime = Instant.parse(elements[5]);
                double low = Double.parseDouble(elements[6]);
                Instant lowTime = Instant.parse(elements[7]);
                double close = Double.parseDouble(elements[8]);
                double volume = Double.parseDouble(elements[9]);
                Integer timeFrame = Integer.parseInt(elements[10]);
                String timeFrameUnit = elements[11];
                String source = elements[12];
                String sourceTimeFrame = elements[13];
                double atr = Double.parseDouble(elements[14]);
                double rsi = Double.parseDouble(elements[15]);
                double ema = Double.parseDouble(elements[16]);
                double sma = Double.parseDouble(elements[17]);
                LocalDateTime creationDateTime = LocalDateTime.parse(elements[18]);

                Candle candle = Candle.builder()
                        .instrumentID(instrumentID)
                        .fromDate(fromDate)
                        .open(open).high(high).highTime(highTime)
                        .low(low)
                        .lowTime(lowTime)
                        .close(close)
                        .volume(volume)
                        .timeFrame(timeFrame)
                        .timeFrameUnit(timeFrameUnit)
                        .source(source)
                        .sourceTimeFrame(sourceTimeFrame)
                        .atr(atr)
                        .rsi(rsi)
                        .ema(ema)
                        .sma(sma)
                        .creationDateTime(creationDateTime)
                        .build();
                candles.add(candle);
            }
            count++;
        }
        return candles;
    }
}
