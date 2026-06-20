package com.hkcapital.portflio.market.structure.it;

import com.hkcapital.portflio.broker.etoro.config.TradingConfiguration;
import com.hkcapital.portflio.etoro.websocket.client.EtoroWebSocketClientAbstract_IT;
import com.hkcapital.portflio.market.indicators.TimeFramesUnit;
import com.hkcapital.portflio.market.structure.MarketStructure;
import com.hkcapital.portflio.market.structure.MarketTypes;
import com.hkcapital.portflio.market.structure.Modus;
import com.hkcapital.portflio.market.structure.PreviousDayMarketRange;
import com.hkcapital.portflio.model.Instrument;
import com.hkcapital.portflio.repository.liveinstrumentfeed.LiveInstrumentFeedRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestClient;

import java.time.Instant;
import java.time.LocalDate;

public class MarketStructure_15_MINS_19_05_2026_Buy_Signals_IT extends EtoroWebSocketClientAbstract_IT
{
    @Autowired
    private LiveInstrumentFeedRepository liveInstrumentFeedRepository;
    @Test
    public void shouldCreateBuySignal_gold_19_05_2026() throws InterruptedException
    {
        RestClient restClient = RestClient.create();
        restClient.post().uri("http://localhost:8081/etoro/init")
                .body(DepositDto.builder().initial(5000)
                        .build()).retrieve().body(String.class);
        liveInstrumentFeedRepository.deleteAll();

        getEtoroCandleService().removeAll();

        Instrument gold = Instrument.builder()
                .etoroInstrumentId(18)
                .build();

        final PreviousDayMarketRange
                priceRange = PreviousDayMarketRange.builder()
                .instrument(gold)
                .date(Instant.now())
                .low(4481.19)
                .high(4589.41)
                .build();

        final Modus modus1Min = Modus.builder().mod(2).subtract(2).build();

        MarketStructure structure1Min = MarketStructure.builder().priceRange(priceRange)
                .modus(Modus.builder()
                        .mod(modus1Min.getMod())
                        .subtract(modus1Min.getSubtract())
                        .build())
                .marketSession(null)
                .objectMapper(objectMapper)
                .instrument(gold)
                .marketDate(LocalDate.now())
                .timeFrameUnit(TimeFramesUnit.MINUTE)
                .intervals(2)
                .timeFrame(1)
                .build();

        final Modus modus5Min = Modus.builder().mod(4).subtract(4).build();

        MarketStructure structure5Min = MarketStructure.builder().priceRange(priceRange)
                .modus(Modus.builder()
                        .mod(modus5Min.getMod())
                        .subtract(modus5Min.getSubtract())
                        .build())
                .marketSession(null)
                .objectMapper(objectMapper)
                .instrument(gold)
                .marketDate(LocalDate.now())
                .childMarketStructure(structure1Min)
                .timeFrameUnit(TimeFramesUnit.MINUTE)
                .intervals(4)
                .timeFrame(5)
                .build();

        final Modus modus15Min = Modus.builder().mod(8).subtract(8).build();

        MarketStructure structure15Min = MarketStructure.builder().priceRange(priceRange)
                .modus(Modus.builder()
                        .mod(modus15Min.getMod())
                        .subtract(modus15Min.getSubtract())
                        .build())
                .marketSession(null)
                .objectMapper(objectMapper)
                .childMarketStructure(structure5Min)
                .instrument(gold)
                .marketDate(LocalDate.now())
                .timeFrameUnit(TimeFramesUnit.MINUTE)
                .intervals(8)
                .timeFrame(15)
                .build();

        final Modus modus30Min = Modus.builder().mod(15).subtract(15).build();
        MarketStructure structure30Min = MarketStructure.builder().priceRange(priceRange)
                .modus(Modus.builder()
                        .mod(modus30Min.getMod())
                        .subtract(modus30Min.getSubtract())
                        .build())
                .marketSession(null)
                .childMarketStructure(structure15Min)
                .objectMapper(objectMapper)
                .instrument(gold)
                .marketDate(LocalDate.now())
                .timeFrameUnit(TimeFramesUnit.MINUTE)
                .timeFrame(30)
                .intervals(15)
                .build();

        final Modus modus1Hour = Modus.builder().mod(30).subtract(30).build();

        MarketStructure structure1Hour = MarketStructure.builder().priceRange(priceRange)
                .modus(Modus.builder()
                        .mod(modus1Hour.getMod())
                        .subtract(modus1Hour.getSubtract())
                        .build())
                .marketSession(null)
                .childMarketStructure(structure30Min)
                .objectMapper(objectMapper)
                .instrument(gold)
                .marketDate(LocalDate.now())
                .timeFrameUnit(TimeFramesUnit.HOUR)
                .timeFrame(1)
                .intervals(30)
                .build();

        final Modus modus4Hour = Modus.builder().mod(40).subtract(40).build();

        MarketStructure structure4Hour = MarketStructure.builder().priceRange(priceRange)
                .modus(Modus.builder()
                        .mod(modus1Hour.getMod())
                        .subtract(modus4Hour.getSubtract())
                        .build())
                .marketSession(null)
                .childMarketStructure(structure1Hour)
                .objectMapper(objectMapper)
                .instrument(gold)
                .marketDate(LocalDate.now())
                .timeFrameUnit(TimeFramesUnit.HOUR)
                .timeFrame(4)
                .intervals(40)
                .build();

        getMarketStructureCache().initDefaultMarket(structure4Hour,
                MarketTypes.GOLD_4_HOUR);


        TradingConfiguration.ACTIVATE_AUTOMATIC_TRADING = Boolean.TRUE;

        marketFeedObserver.addMarketFeedSubscriber(marketFeedDbWriter);

        connect(gold);

        Thread.sleep(10000 * 16000);
    }
}
