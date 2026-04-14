package com.hkcapital.portoflio.indicators;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hkcapital.portoflio.service.api.etoro.websocket.LiveInstrumentRate;
import com.hkcapital.portoflio.service.api.etoro.websocket.LivePriceResponseWrapper;
import com.hkcapital.portoflio.service.api.etoro.websocket.Message;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@Import({ObjectMapper.class})
class CandleBuilderTest
{
    private final static String GOLD_NASDAQ100_LIVE_FEED_FILE_PATH = //
            "D:/portfolio-pnl-simulator/src/test/data/livefeed-etoro/gold_1_minute_candle.log";

    @Test
    public void shouldBuildCandles()
    {
        CandleBuilder candleBuilder = CandleBuilder.build();

        Tick t1 = Tick.builder().instrument("18").time(Instant.parse("2007-12-03T10:15:00.00Z")).val(5400).build();
        Tick t2 = Tick.builder().instrument("18").time(Instant.parse("2007-12-03T10:15:01.00Z")).val(5401.53).build();
        Tick t3 = Tick.builder().instrument("18").time(Instant.parse("2007-12-03T10:15:02.00Z")).val(5405.53).build();
        Tick t4 = Tick.builder().instrument("18").time(Instant.parse("2007-12-03T10:15:59.00Z")).val(5403.51).build();

        Tick t5 = Tick.builder().instrument("18").time(Instant.parse("2007-12-03T10:16:00.00Z")).val(5410).build();
        Tick t6 = Tick.builder().instrument("18").time(Instant.parse("2007-12-03T10:16:01.00Z")).val(5411.53).build();
        Tick t7 = Tick.builder().instrument("18").time(Instant.parse("2007-12-03T10:16:02.00Z")).val(5405.47).build();
        Tick t8 = Tick.builder().instrument("18").time(Instant.parse("2007-12-03T10:16:59.00Z")).val(5415.56).build();

        candleBuilder.addTick(t1).addTick(t2).addTick(t3).addTick(t4).addTick(t5).addTick(t6).addTick(t7).addTick(t8);

        List<Candle> candles = candleBuilder.of(Unit.MINUTE);

        assertEquals(candles.size(), 2);

        Candle candle15 = candles.get(0);
        Candle candle16 = candles.get(1);

        assertEquals(5400, candle15.getLow());
        assertEquals(5405.53, candle15.getHigh());
        assertEquals(5403.51, candle15.getClose());

        assertEquals(5405.47, candle16.getLow());
        assertEquals(5415.56, candle16.getHigh());
        assertEquals(5415.56, candle16.getClose());

    }


    @Test
    public void testLiveEtoroRate() throws IOException
    {
        CandleBuilder candleBuilder = CandleBuilder.build();
        String expectedText = "{\"messages\":[{";
        ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();
        LivePriceResponseWrapper livePriceResponseWrapper;

        try (BufferedReader br = new BufferedReader(new FileReader(GOLD_NASDAQ100_LIVE_FEED_FILE_PATH)))
        {
            String line;
            while ((line = br.readLine()) != null)
            {
                if (!line.contains(expectedText)) continue;
                livePriceResponseWrapper = objectMapper.readValue(line, LivePriceResponseWrapper.class);
                for (Message message : livePriceResponseWrapper.getMessages())
                {
                    LiveInstrumentRate rate = objectMapper.readValue(message.getContent(), LiveInstrumentRate.class);
                    Tick tick = Tick.builder()
                            .instrument(rate.getInstrumentId().toString()) //
                            .time(rate.getDate())//
                            .val(rate.getAsk()).build();
                    candleBuilder.addTick(tick);
                }
            }
        }

        List<Candle> candles = candleBuilder.of(Unit.MINUTE);
        assertEquals(candles.size(), 2);

        Candle candle15 = candles.get(0);
        Candle candle16 = candles.get(1);

        assertEquals(4999.15, candle15.getLow());
        assertEquals(5003.15, candle15.getHigh());
        assertEquals(4999.15, candle15.getClose());

        assertEquals(4981.55, candle16.getLow());
        assertEquals(5017.15, candle16.getHigh());
        assertEquals(4981.55, candle16.getClose());
    }


    @Test
    public void shouldExtractFirstCandle() throws IOException
    {
        CandleBuilder candleBuilder = CandleBuilder.build();
        String expectedText = "{\"messages\":[{";
        ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();
        LivePriceResponseWrapper livePriceResponseWrapper;

        try (BufferedReader br = new BufferedReader(new FileReader(GOLD_NASDAQ100_LIVE_FEED_FILE_PATH)))
        {
            String line;
            while ((line = br.readLine()) != null)
            {
                if (!line.contains(expectedText)) continue;
                livePriceResponseWrapper = objectMapper.readValue(line, LivePriceResponseWrapper.class);
                for (Message message : livePriceResponseWrapper.getMessages())
                {
                    LiveInstrumentRate rate = objectMapper.readValue(message.getContent(), LiveInstrumentRate.class);
                    Tick tick = Tick.builder()
                            .instrument(rate.getInstrumentId().toString()) //
                            .time(rate.getDate())//
                            .val(rate.getAsk()).build();
                    candleBuilder.addTick(tick);
                }
            }
        }

        List<Candle> candles = candleBuilder.fromTo(Unit.MINUTE, 2);
        assertEquals(candles.size(), 2);

        Candle candle15 = candles.get(0);

        assertEquals(4999.15, candle15.getLow());
        assertEquals(5003.15, candle15.getHigh());
        assertEquals(4999.15, candle15.getClose());

    }

    @Test
    public void btcOneMinutLiveFeed() throws IOException
    {
        CandleBuilder candleBuilder = CandleBuilder.build();
        String expectedText = "{\"messages\":[{";
        ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();
        LivePriceResponseWrapper livePriceResponseWrapper;

        try (BufferedReader br = new BufferedReader(new FileReader("D:\\portfolio-pnl-simulator\\src\\test\\data\\livefeed-etoro\\gold_1_minute_multiple_candle.log")))
        {
            String line;
            while ((line = br.readLine()) != null)
            {
                if (!line.contains(expectedText)) continue;
                livePriceResponseWrapper = objectMapper.readValue(line, LivePriceResponseWrapper.class);
                for (Message message : livePriceResponseWrapper.getMessages())
                {
                    LiveInstrumentRate rate = objectMapper.readValue(message.getContent(), LiveInstrumentRate.class);
                    if(rate.getInstrumentId() != null) {
                        Tick tick = Tick.builder()
                                .instrument(rate.getInstrumentId().toString()) //
                                .time(rate.getDate())//
                                .val(rate.getAsk()).build();
                        candleBuilder.addTick(tick);
                    }

                }
            }
        }

        List<Candle> candles = candleBuilder.of(Unit.MINUTE);
        candles.forEach(System.out::println);


    }


}