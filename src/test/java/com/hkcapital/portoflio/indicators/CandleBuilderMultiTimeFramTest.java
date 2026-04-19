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

import static com.hkcapital.portoflio.indicators.Unit.MINUTE;

@SpringBootTest
@DisplayName("Should test different candle generation scenarios")
class CandleBuilderMultiTimeFramTest extends CandleBuilderAbstract
{

    @Autowired
    private LiveInstrumentFeedRepository liveInstrumentFeedRepository;

    @Test
    @DisplayName("Should aggregate market ticks into a 30 minutes candle")
    public void shouldAggregateMarketTicksInMinuteCandles() throws IOException
    {


        List<LiveInstrumentFeed> feed = liveInstrumentFeedRepository
                .findByFeedDateBetween(Instant.parse("2026-04-08T00:00:00.0Z"), Instant.parse("2026-04-08T23:59:59.0Z"));

        CandleBuilder builderOf_15_Min = CandleBuilder.build().ofTimeFrame(MINUTE).ofInterval(30);

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
                builderOf_15_Min.addAndUpdateCandle(candle);
            }
        });
        builderOf_15_Min.candles().forEach(c -> System.out.println(c));
    }


}