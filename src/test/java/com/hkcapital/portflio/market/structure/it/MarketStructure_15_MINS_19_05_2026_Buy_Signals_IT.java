package com.hkcapital.portflio.market.structure.it;

import com.hkcapital.portflio.broker.etoro.config.TradingConfiguration;
import com.hkcapital.portflio.broker.etoro.simulation.SimulationHelper;
import com.hkcapital.portflio.etoro.websocket.client.EtoroWebSocketClientAbstract_IT;
import com.hkcapital.portflio.market.indicators.TimeFramesUnit;
import com.hkcapital.portflio.market.structure.*;
import com.hkcapital.portflio.model.Instrument;
import com.hkcapital.portflio.repository.liveinstrumentfeed.LiveInstrumentFeedRepository;
import com.hkcapital.portflio.repository.orders.etoro.EtoroOrderRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.http.WebSocket;
import java.nio.file.Path;
import java.time.LocalDate;

public class MarketStructure_15_MINS_19_05_2026_Buy_Signals_IT extends EtoroWebSocketClientAbstract_IT
{
    @Autowired
    private LiveInstrumentFeedRepository liveInstrumentFeedRepository;
    @Autowired
    private EtoroOrderRepository etoroOrderRepository;

    final static String previousDayCandle = "2026-07-30";
    final static String tradingSession = "2026-07-31";
    private static final String PATH = "D:/gold_data/" + previousDayCandle + "/candle/gold_candle_"
            .concat(previousDayCandle).concat(".csv");

    @Test
    public void shouldCreateBuySignal_gold_19_05_2026() throws InterruptedException
    {

        CandleHelper candleHelper = new CandleHelper(Path.of(PATH));
        //SimulationHelper.cleanAndInitPortfolio(5000);
        etoroOrderRepository.deleteAll();
        liveInstrumentFeedRepository.deleteAll();

        getEtoroCandleService().removeAll();

        Instrument gold = Instrument.builder()
                .etoroInstrumentId(18)
                .build();

        final PreviousDayMarketRange priceRange = candleHelper.getPreviousDayMarketRange();

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

        structure1Min.init(candleHelper.candleListOf(1, TimeFramesUnit.MINUTE.getUnit()));

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
        structure1Min.init(candleHelper.candleListOf(5, TimeFramesUnit.MINUTE.getUnit()));

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

        structure15Min.init(candleHelper.candleListOf(15, TimeFramesUnit.MINUTE.getUnit()));

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
        structure30Min.init(candleHelper.candleListOf(30, TimeFramesUnit.MINUTE.getUnit()));

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
        structure1Hour.init(candleHelper.candleListOf(1, TimeFramesUnit.HOUR.getUnit()));

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
        structure4Hour.init(candleHelper.candleListOf(4, TimeFramesUnit.HOUR.getUnit()));

        MarketStructureCache cache = getMarketStructureCache();

        cache.initDefaultMarket(structure4Hour,
                MarketTypes.GOLD_4_HOUR);

        TradingConfiguration.ACTIVATE_AUTOMATIC_TRADING = Boolean.TRUE;
        marketFeedObserver.addMarketFeedSubscriber(marketFeedDbWriter);
        WebSocket ws = connect(gold);
        String data = "{ data:  { value : %s} }".formatted(tradingSession);
        subscribeDataFeed(ws, data);

        while (true)
        {
//            String portfolValue = restClient.get()
//                    .uri("http://localhost:8081/etoro/portfolio-value")
//                    .retrieve().body(String.class);
           // System.out.println(portfolValue);
        }

    }
}
