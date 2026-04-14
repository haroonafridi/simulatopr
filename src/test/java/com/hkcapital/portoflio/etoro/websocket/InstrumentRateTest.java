package com.hkcapital.portoflio.etoro.websocket;

import com.hkcapital.portoflio.etoro.EtoroAbstractTest;
import com.hkcapital.portoflio.service.api.etoro.websocket.LiveInstrumentRate;
import com.hkcapital.portoflio.service.api.etoro.websocket.LivePriceResponseWrapper;
import com.hkcapital.portoflio.service.api.etoro.websocket.Message;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class InstrumentRateTest extends EtoroAbstractTest
{
    private final static String GOLD_NASDAQ100_LIVE_FEED_FILE_PATH = //
            "D:/portfolio-pnl-simulator/src/test/data/livefeed-etoro/nasdaq_gold_instrument-message.log";

    @Test
    void instrumentRateBasicTest() throws IOException
    {

        List<LiveInstrumentRate> liveInstrumentRateList = new ArrayList<>();
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
                    liveInstrumentRateList.add(objectMapper.readValue(message.getContent(), LiveInstrumentRate.class));
                }
            }
        }
        assertEquals(2, liveInstrumentRateList.size());
        assertGoldLiveRateFeed(liveInstrumentRateList.get(0));
        assertNASDAQ100LiveRateFeed(liveInstrumentRateList.get(1));
    }

    private static void assertNASDAQ100LiveRateFeed(LiveInstrumentRate nasdaq100Rate)
    {
        assertEquals(28, nasdaq100Rate.getInstrumentId(), "InstrumentID is not the same as expected");
        assertEquals(24625.05d, nasdaq100Rate.getAsk(), "Ask is not the same as expected");
        assertEquals(24621.35d, nasdaq100Rate.getBid(), "Bid is not the same as expected");
        assertEquals(24622.4d, nasdaq100Rate.getLastExecution(), "LastExecution is not the same as expected");
        assertEquals(1, nasdaq100Rate.getConversionRateAsk(), "ConversionRateAsk is not the same as expected");
        assertEquals(1, nasdaq100Rate.getConversionRateBid(), "ConversionRateBid is not the same as expected");
        assertEquals(Instant.parse("2026-03-16T23:11:49.8866791Z"), nasdaq100Rate.getDate(), "Date is not the same as expected");
        assertEquals(24621.35d, nasdaq100Rate.getNewUnitMargin(), "NewUnitMargin is not the same as expected");
        assertEquals(24625.05d, nasdaq100Rate.getUnitMarginAsk(), "UnitMarginAsk is not the same as expected");
        assertEquals(24621.35d, nasdaq100Rate.getUnitMarginBid(), "UnitMarginBid is not the same as expected");
        assertEquals(135650796181L, nasdaq100Rate.getPriceRateId(), "PriceRateID is not the same as expected");
        assertEquals(24623.2d, nasdaq100Rate.getBidDiscounted(), "BidDiscounted is not the same as expected");
        assertEquals(24623.2d, nasdaq100Rate.getAskDiscounted(), "AskDiscounted is not the same as expected");
        assertEquals(24623.2d, nasdaq100Rate.getUnitMarginBidDiscounted(), "UnitMarginBidDiscounted is not the same as expected");
        assertEquals(24623.2d, nasdaq100Rate.getUnitMarginAskDiscounted(), "UnitMarginAskDiscounted is not the same as expected");
        assertTrue(nasdaq100Rate.getIsInstrumentActive(), "IsInstrumentActive is not the same as expected");
        assertEquals(24630.9d, nasdaq100Rate.getOfficialClosingPrice(), "OfficialClosingPrice is not the same as expected");
        assertTrue(nasdaq100Rate.getIsMarketOpen(), "IsMarketOpen is not the same as expected");
        assertTrue(nasdaq100Rate.getAllowBuy(), "AllowBuy is not the same as expected");
        assertTrue(nasdaq100Rate.getAllowSell(), "AllowSell is not the same as expected");
        assertEquals(1000, nasdaq100Rate.getMaxPositionUnits(), "MaxPositionUnits is not the same as expected");
        assertFalse(nasdaq100Rate.getIsExchangeOpen(), "IsExchangeOpen is not the same as expected");
        assertEquals(0, nasdaq100Rate.getDelayedAsk(), "DelayedAsk is not the same as expected");
        assertEquals(0, nasdaq100Rate.getDelayedBid(), "DelayedBid is not the same as expected");
        assertEquals(2, nasdaq100Rate.getAvailabilityReason(), "AvailabilityReason is not the same as expected");
        assertFalse(nasdaq100Rate.getIsOfficialClosingPrice(), "IsOfficialClosingPrice is not the same as expected");
    }

    private static void assertGoldLiveRateFeed(LiveInstrumentRate goldLiveRateFeed)
    {
        assertEquals(18, goldLiveRateFeed.getInstrumentId(), "InstrumentID is not the same as expected");
        assertEquals(5006.15d, goldLiveRateFeed.getAsk(), "Ask is not the same as expected");
        assertEquals(5004.69d, goldLiveRateFeed.getBid(), "Bid is not the same as expected");
        assertEquals(5005.32d, goldLiveRateFeed.getLastExecution(), "LastExecution is not the same as expected");
        assertEquals(1, goldLiveRateFeed.getConversionRateAsk(), "ConversionRateAsk is not the same as expected");
        assertEquals(1, goldLiveRateFeed.getConversionRateBid(), "ConversionRateBid is not the same as expected");
        assertEquals(Instant.parse("2026-03-16T23:11:49.1874723Z"), goldLiveRateFeed.getDate(), "Date is not the same as expected");
        assertEquals(5004.69, goldLiveRateFeed.getNewUnitMargin(), "NewUnitMargin is not the same as expected");
        assertEquals(5006.15, goldLiveRateFeed.getUnitMarginAsk(), "UnitMarginAsk is not the same as expected");
        assertEquals(5004.69, goldLiveRateFeed.getUnitMarginBid(), "UnitMarginBid is not the same as expected");
        assertEquals(135650796163L, goldLiveRateFeed.getPriceRateId(), "PriceRateID is not the same as expected");
        assertEquals(5005.32d, goldLiveRateFeed.getBidDiscounted(), "BidDiscounted is not the same as expected");
        assertEquals(5005.52d, goldLiveRateFeed.getAskDiscounted(), "AskDiscounted is not the same as expected");
        assertEquals(5005.32d, goldLiveRateFeed.getUnitMarginBidDiscounted(), "UnitMarginBidDiscounted is not the same as expected");
        assertEquals(5005.52d, goldLiveRateFeed.getUnitMarginAskDiscounted(), "UnitMarginAskDiscounted is not the same as expected");
        assertTrue(goldLiveRateFeed.getIsInstrumentActive(), "IsInstrumentActive is not the same as expected");
        assertEquals(5009.46d, goldLiveRateFeed.getOfficialClosingPrice(), "OfficialClosingPrice is not the same as expected");
        assertTrue(goldLiveRateFeed.getIsMarketOpen(), "IsMarketOpen is not the same as expected");
        assertTrue(goldLiveRateFeed.getAllowBuy(), "AllowBuy is not the same as expected");
        assertTrue(goldLiveRateFeed.getAllowSell(), "AllowSell is not the same as expected");
        assertEquals(6500, goldLiveRateFeed.getMaxPositionUnits(), "MaxPositionUnits is not the same as expected");
        assertFalse(goldLiveRateFeed.getIsExchangeOpen(), "IsExchangeOpen is not the same as expected");
        assertEquals(0, goldLiveRateFeed.getDelayedAsk(), "DelayedAsk is not the same as expected");
        assertEquals(0, goldLiveRateFeed.getDelayedBid(), "DelayedBid is not the same as expected");
        assertEquals(2, goldLiveRateFeed.getAvailabilityReason(), "AvailabilityReason is not the same as expected");
        assertTrue(goldLiveRateFeed.getIsOfficialClosingPrice(), "IsOfficialClosingPrice is not the same as expected");
    }
}
