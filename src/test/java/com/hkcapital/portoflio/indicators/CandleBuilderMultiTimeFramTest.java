package com.hkcapital.portoflio.indicators;

import com.hkcapital.portoflio.model.LiveInstrumentFeed;
import com.hkcapital.portoflio.repository.liveinstrumentfeed.LiveInstrumentFeedRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.time.Instant;
import java.util.List;

import static com.hkcapital.portoflio.indicators.Unit.HOUR;
import static com.hkcapital.portoflio.indicators.Unit.MINUTE;

@SpringBootTest
@DisplayName("Should test different candle generation scenarios")
class CandleBuilderMultiTimeFrameTest extends CandleBuilderAbstract
{
    @Autowired
    private LiveInstrumentFeedRepository liveInstrumentFeedRepository;

    @Test
    @DisplayName("Should aggregate market ticks into a 01 minutes candle")
    public void shouldAggregateMarketTicksIn1MinuteCandles() throws IOException
    {

        List<LiveInstrumentFeed> feed = liveInstrumentFeedRepository
                .findByFeedDateBetween(Instant.parse("2026-04-08T00:00:00.0Z"), Instant.parse("2026-04-08T23:59:59.0Z"));

        CandleBuilder builderOf_05_Min = CandleBuilder.build().ofTimeFrame(MINUTE).ofInterval(1);

        feed.forEach(rate ->
        {
            if (rate.getInstrumentId() != null && rate.getAsk() != null && rate.getFeedDate() != null)
            {
                Tick tick = Tick.builder().instrument(rate.getInstrumentId().toString())
                        .val(rate.getAsk())
                        .time(rate.getFeedDate())
                        .build();
                Candle candle = Candle.builder()
                        .instrument(tick.getInstrument())
                        .high(tick.getVal())
                        .open(tick.getVal())
                        .low(tick.getVal())
                        .close(tick.getVal())
                        .time(tick.getTime())
                        .interval(1)
                        .unit(MINUTE)
                        .build();
                builderOf_05_Min.addAndUpdateCandle(candle);
            }
        });
    }

    @Test
    @DisplayName("Should aggregate market ticks into a 05 minutes candle")
    public void shouldAggregateMarketTicksIn5MinuteCandles() throws IOException
    {

        List<LiveInstrumentFeed> feed = liveInstrumentFeedRepository
                .findByFeedDateBetween(Instant.parse("2026-04-08T00:00:00.0Z"), Instant.parse("2026-04-08T23:59:59.0Z"));

        CandleBuilder builderOf_05_Min = CandleBuilder.build().ofTimeFrame(MINUTE).ofInterval(5);

        feed.forEach(rate ->
        {
            if (rate.getInstrumentId() != null && rate.getAsk() != null && rate.getFeedDate() != null)
            {
                Tick tick = Tick.builder().instrument(rate.getInstrumentId().toString())
                        .val(rate.getAsk())
                        .time(rate.getFeedDate())
                        .build();
                Candle candle = Candle.builder()
                        .instrument(tick.getInstrument())
                        .high(tick.getVal())
                        .open(tick.getVal())
                        .low(tick.getVal())
                        .close(tick.getVal())
                        .time(tick.getTime())
                        .interval(5)
                        .unit(MINUTE)
                        .build();
                builderOf_05_Min.addAndUpdateCandle(candle);
            }
        });
    }

    @Test
    @DisplayName("Should aggregate market ticks into a 15 minutes candle")
    public void shouldAggregateMarketTicksIn30MinuteCandles() throws IOException
    {

        List<LiveInstrumentFeed> feed = liveInstrumentFeedRepository
                .findByFeedDateBetween(Instant.parse("2026-04-08T00:00:00.0Z"), Instant.parse("2026-04-08T23:59:59.0Z"));

        CandleBuilder builderOf_15_Min = CandleBuilder.build().ofTimeFrame(MINUTE).ofInterval(15);

        feed.forEach(rate ->
        {
            if (rate.getInstrumentId() != null && rate.getAsk() != null && rate.getFeedDate() != null)
            {
                Tick tick = Tick.builder().instrument(rate.getInstrumentId().toString())
                        .val(rate.getAsk())
                        .time(rate.getFeedDate())
                        .build();
                Candle candle = Candle.builder()
                        .instrument(tick.getInstrument())
                        .high(tick.getVal())
                        .open(tick.getVal())
                        .low(tick.getVal())
                        .close(tick.getVal())
                        .time(tick.getTime())
                        .interval(15)
                        .unit(MINUTE)
                        .build();
                builderOf_15_Min.addAndUpdateCandle(candle);
            }
        });
    }

    @Test
    @DisplayName("Should aggregate market ticks into a 30 minutes candle")
    public void shouldAggregateMarketTicksIn15MinuteCandles() throws IOException
    {

        List<LiveInstrumentFeed> feed = liveInstrumentFeedRepository
                .findByFeedDateBetween(Instant.parse("2026-04-08T00:00:00.0Z"), Instant.parse("2026-04-08T23:59:59.0Z"));

        CandleBuilder builderOf_30_Min = CandleBuilder.build().ofTimeFrame(MINUTE).ofInterval(30);

        feed.forEach(rate ->
        {
            if (rate.getInstrumentId() != null && rate.getAsk() != null && rate.getFeedDate() != null)
            {
                Tick tick = Tick.builder().instrument(rate.getInstrumentId().toString())
                        .val(rate.getAsk())
                        .time(rate.getFeedDate())
                        .build();
                Candle candle = Candle.builder()
                        .instrument(tick.getInstrument())
                        .high(tick.getVal())
                        .open(tick.getVal())
                        .low(tick.getVal())
                        .close(tick.getVal())
                        .time(tick.getTime())
                        .interval(30)
                        .unit(MINUTE)
                        .build();
                builderOf_30_Min.addAndUpdateCandle(candle);
            }
        });
    }

    @Test
    @DisplayName("Should aggregate market ticks into a 1 hour candle")
    public void shouldAggregateMarketTicksIn1HourCandles() throws IOException
    {

        List<LiveInstrumentFeed> feed = liveInstrumentFeedRepository
                .findByFeedDateBetween(Instant.parse("2026-04-08T00:00:00.0Z"), Instant.parse("2026-04-08T23:59:59.0Z"));

        CandleBuilder builderOf_01_Hour = CandleBuilder.build().ofTimeFrame(HOUR).ofInterval(1);

        feed.forEach(rate ->
        {
            if (rate.getInstrumentId() != null && rate.getAsk() != null && rate.getFeedDate() != null)
            {
                Tick tick = Tick.builder().instrument(rate.getInstrumentId().toString())
                        .val(rate.getAsk())
                        .time(rate.getFeedDate())
                        .build();
                Candle candle = Candle.builder()
                        .instrument(tick.getInstrument())
                        .high(tick.getVal())
                        .open(tick.getVal())
                        .low(tick.getVal())
                        .close(tick.getVal())
                        .time(tick.getTime())
                        .interval(1)
                        .unit(HOUR)
                        .build();
                builderOf_01_Hour.addAndUpdateCandle(candle);
            }
        });
    }


}