package com.hkcapital.portoflio.indicators;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hkcapital.portoflio.service.api.etoro.websocket.LiveInstrumentRate;
import com.hkcapital.portoflio.service.api.etoro.websocket.Message;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static com.hkcapital.portoflio.indicators.Unit.MINUTE;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@Import({ObjectMapper.class})
@DisplayName("Should test different candle generation scenarios")
class CandleBuilderTest extends CandleBuilderAbstract
{
    private final static String gold_1_minute_candle = "D:/portfolio-pnl-simulator/src/test/data/livefeed-etoro/gold_1_minute_candle.log";
    private final static String gold_1_minute_multiple_candle = "D:\\portfolio-pnl-simulator\\src\\test\\data\\livefeed-etoro\\gold_1_minute_multiple_candle.log";
    private final static String gold_live_ticks_etoro = "D:\\portfolio-pnl-simulator\\src\\test\\data\\livefeed-etoro\\gole_live_ticks_etoro.csv";

    @Test
    @DisplayName("Should aggregate market ticks into a minute candle")
    public void shouldAggregateMarketTicksInMinuteCandles()
    {
        List<Candle> candles = CandleBuilder.build().ofTimeFrame(MINUTE).ofInterval(1)//
                .addAndUpdateCandle(toOneMinuteCandle(Tick.builder().instrument(GOLD_ETORO_INSTRUMENT)
                        .time(Instant.parse("2007-12-03T10:15:00.00Z")).val(5400).build()))//
                .addAndUpdateCandle(toOneMinuteCandle(Tick.builder().instrument(GOLD_ETORO_INSTRUMENT)
                        .time(Instant.parse("2007-12-03T10:15:01.00Z")).val(5401.53).build()))//
                .addAndUpdateCandle(toOneMinuteCandle(Tick.builder().instrument(GOLD_ETORO_INSTRUMENT)
                        .time(Instant.parse("2007-12-03T10:15:02.00Z")).val(5405.53).build()))//
                .addAndUpdateCandle(toOneMinuteCandle(Tick.builder().instrument(GOLD_ETORO_INSTRUMENT)
                        .time(Instant.parse("2007-12-03T10:15:59.00Z")).val(5403.51).build()))//
                .addAndUpdateCandle(toOneMinuteCandle(Tick.builder().instrument(GOLD_ETORO_INSTRUMENT)
                        .time(Instant.parse("2007-12-03T10:16:00.00Z")).val(5410).build()))//
                .addAndUpdateCandle(toOneMinuteCandle(Tick.builder().instrument(GOLD_ETORO_INSTRUMENT)
                        .time(Instant.parse("2007-12-03T10:16:01.00Z")).val(5411.53).build()))//
                .addAndUpdateCandle(toOneMinuteCandle(Tick.builder().instrument(GOLD_ETORO_INSTRUMENT)
                        .time(Instant.parse("2007-12-03T10:16:02.00Z")).val(5405.47).build()))//
                .addAndUpdateCandle(toOneMinuteCandle(Tick.builder().instrument(GOLD_ETORO_INSTRUMENT)
                        .time(Instant.parse("2007-12-03T10:16:59.00Z")).val(5415.56).build()))//
                .candles();

        assertEquals(candles.size(), 2);

        assertEquals(5400, candles.get(0).getLow());
        assertEquals(5405.53, candles.get(0).getHigh());
        assertEquals(5403.51, candles.get(0).getClose());
        assertEquals(5405.47, candles.get(1).getLow());
        assertEquals(5415.56, candles.get(1).getHigh());
        assertEquals(5415.56, candles.get(1).getClose());
    }

    @Test
    public void shouldBuildOneMinuteTwoCandles() throws IOException
    {
        CandleBuilder candleBuilder = CandleBuilder.build().ofTimeFrame(MINUTE).ofInterval(1);
        for (String line : loadData(gold_1_minute_candle))
        {
            if (!line.contains(expectedText)) continue;
            for (Message message : messageFrom(line))
            {
                candleBuilder.addAndUpdateCandle(toOneMinuteCandle(tickFromRate(rateFromMessage(message))));
            }
        }

        List<Candle> candles = candleBuilder.candles();
        assertEquals(candles.size(), 2);

        assertEquals(4999.15, candles.get(0).getLow());
        assertEquals(5003.15, candles.get(0).getHigh());
        assertEquals(4999.15, candles.get(0).getClose());

        assertEquals(4981.55, candles.get(1).getLow());
        assertEquals(5017.15, candles.get(1).getHigh());
        assertEquals(4981.55, candles.get(1).getClose());
    }

    @Test
    public void shouldExtractFirstCandle() throws IOException
    {
        CandleBuilder candleBuilder = CandleBuilder.build().ofTimeFrame(MINUTE).ofInterval(1);
        for (String line : loadData(gold_1_minute_candle))
        {
            if (!line.contains(expectedText)) continue;
            for (Message message : messageFrom(line))
            {
                candleBuilder.addAndUpdateCandle(toOneMinuteCandle(tickFromRate(rateFromMessage(message))));
            }
        }
        List<Candle> candles = candleBuilder.fromTo(MINUTE, 2);
        assertEquals(candles.size(), 2);
        Candle candle15 = candles.get(0);
        assertEquals(4999.15, candle15.getLow());
        assertEquals(5003.15, candle15.getHigh());
        assertEquals(4999.15, candle15.getClose());

    }

    @Test
    public void btcOneMinuteLiveFeed() throws IOException
    {

        CandleBuilder candleBuilder = CandleBuilder.build().ofTimeFrame(MINUTE).ofInterval(1);

        for (String line : loadData(gold_1_minute_multiple_candle))
        {
            if (!line.contains(expectedText)) continue;

            for (Message message : messageFrom(line))
            {
                LiveInstrumentRate rate = rateFromMessage(message);
                if (rate.getInstrumentId() != null)
                {
                    candleBuilder.addAndUpdateCandle(toOneMinuteCandle(tickFromRate(rateFromMessage(message))));
                }
            }
        }
        List<Candle> candles = candleBuilder.candles();
        candles.forEach(System.out::println);
    }

    @Test
    public void shouldBuildOneMinuteCandlesFromCSV() throws IOException
    {
        CandleBuilder candleBuilder = CandleBuilder.build().ofTimeFrame(MINUTE).ofInterval(1);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS");
        for (String line : loadData(gold_live_ticks_etoro))
        {
            String[] data = line.split(",");
            LocalDateTime localDateTime = LocalDateTime.parse(data[2].replace("\"", ""), formatter);
            Tick tick = Tick.builder().instrument(data[0]) //
                    .val(Double.valueOf(data[1])) //
                    .time(localDateTime.toInstant(ZoneOffset.UTC)).build();
            candleBuilder.addAndUpdateCandle(toOneMinuteCandle(tick));
        }
        List<Candle> candles = candleBuilder.candles();
        Assertions.assertEquals(20, candles.size());
        assertOHLC("Candle 0", 4678.96, 4677.91, 4679.41, 4679.41, candles.get(0));
        assertOHLC("Candle 1", 4679.35, 4677.68, 4679.35, 4678.15, candles.get(1));
        assertOHLC("Candle 2", 4678.43, 4677.19, 4679.92, 4677.75, candles.get(2));
        assertOHLC("Candle 3", 4678.06, 4677.76, 4680.28, 4679.29, candles.get(3));
        assertOHLC("Candle 4", 4679.22, 4678.28, 4680.55, 4679.37, candles.get(4));
        assertOHLC("Candle 5", 4679.35, 4678.29, 4680.01, 4678.75, candles.get(5));
        assertOHLC("Candle 6", 4678.69, 4678.19, 4680.21, 4680.15, candles.get(6));
        assertOHLC("Candle 7", 4680.39, 4680.39, 4682.92, 4682.6, candles.get(7));
        assertOHLC("Candle 8", 4682.62, 4682.42, 4683.86, 4683.26, candles.get(8));
        assertOHLC("Candle 9", 4683.31, 4683.14, 4685.48, 4684.72, candles.get(9));
        assertOHLC("Candle 10", 4684.66, 4683.75, 4686.73, 4683.99, candles.get(10));
        assertOHLC("Candle 11", 4683.94, 4682.69, 4684.04, 4682.74, candles.get(11));
        assertOHLC("Candle 12", 4682.75, 4681.45, 4683.46, 4682.69, candles.get(12));
        assertOHLC("Candle 13", 4682.45, 4682.2, 4684.13, 4683.84, candles.get(13));
        assertOHLC("Candle 14", 4683.73, 4682.77, 4685.8, 4685.58, candles.get(14));
        assertOHLC("Candle 15", 4685.53, 4685.05, 4686.74, 4685.26, candles.get(15));
        assertOHLC("Candle 16", 4685.22, 4683.77, 4685.22, 4684.48, candles.get(16));
        assertOHLC("Candle 17", 4684.24, 4684.05, 4685.5, 4684.86, candles.get(17));
        assertOHLC("Candle 18", 4684.76, 4679.94, 4685.03, 4680.44, candles.get(18));
        assertOHLC("Candle 19", 4680.5, 4677.9, 4681.06, 4678.63, candles.get(19));
    }


    private void assertOHLC(String message, double open, double low, double high, double close, Candle candle)
    {
        assertAll(message, () ->
        {
            Assertions.assertEquals(open, candle.getOpen());
            Assertions.assertEquals(low, candle.getLow());
            Assertions.assertEquals(high, candle.getHigh());
            Assertions.assertEquals(close, candle.getClose());
        });
    }


}