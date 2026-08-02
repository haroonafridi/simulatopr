package com.hkcapital.portflio.market.structure.it;

import com.hkcapital.portflio.broker.etoro.config.TradingConfiguration;
import com.hkcapital.portflio.etoro.websocket.client.EtoroWebSocketClientAbstract_IT;
import com.hkcapital.portflio.market.indicators.TimeFramesUnit;
import com.hkcapital.portflio.market.structure.*;
import com.hkcapital.portflio.model.*;
import com.hkcapital.portflio.repository.liveinstrumentfeed.LiveInstrumentFeedRepository;
import com.hkcapital.portflio.repository.orders.etoro.EtoroOrderRepository;
import com.hkcapital.portflio.service.configuration.ConfigurationService;
import com.hkcapital.portflio.service.marketconditions.MarketConditionsService;
import com.hkcapital.portflio.service.positions.PositionService;
import com.hkcapital.portflio.service.srmatrix.SRMatrixService;
import com.hkcapital.portflio.service.strategy.StrategyService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestClient;

import java.net.http.WebSocket;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class MarketStructure_15_MINS_31_07_2026_Buy_Signals_IT extends EtoroWebSocketClientAbstract_IT
{
    @Autowired
    private LiveInstrumentFeedRepository liveInstrumentFeedRepository;
    @Autowired
    private EtoroOrderRepository etoroOrderRepository;
    @Autowired
    private StrategyService strategyService;
    @Autowired
    private MarketConditionsService marketConditionsService;
    @Autowired
    private ConfigurationService configurationService;
    @Autowired
    private SRMatrixService sRMatrixService;
    @Autowired
    private PositionService positionService;
    final static String previousDayCandle = "2026-07-30";
    final static String tradingSession = "2026-07-31";
    private static final String PATH = "D:/gold_data/" + previousDayCandle + "/candle/gold_candle_"
            .concat(previousDayCandle).concat(".csv");

    @Test
    public void shouldCreateBuySignal_gold_31_07_2026() throws InterruptedException
    {

        CandleHelper candleHelper = new CandleHelper(Path.of(PATH));

        RestClient restClient = RestClient.create();

        restClient.post().uri("http://localhost:8081/etoro/init")
                .body(DepositDto.builder().initial(5000)
                        .build()).retrieve().body(String.class);
        etoroOrderRepository.deleteAll();
        liveInstrumentFeedRepository.deleteAll();
        positionService.removeAll();
        configurationService.removeAll();
        marketConditionsService.removeAll();
        sRMatrixService.removeAll();
        strategyService.removeAll();
        getEtoroCandleService().removeAll();

        Instrument gold = instrumentService.addInstrument(Instrument.builder()
                .name("GOLD")
                .instrumentDesc("Gold Test")
                .active(true)
                .maxSlippage(1.75)
                .etoroInstrumentId(18)
                .build());

        Strategy strategy = strategyService.addStrategy(Strategy.builder()
                .active(true)
                .description("Gold Test Strategy")
                .name("Gold Test Strategy")
                .capitalAllocated(10000d)
                .creationDate(LocalDateTime.now()).build());

        MarketConditions marketConditions = marketConditionsService.addMarketCondition(MarketConditions.builder()
                .instrument(gold)
                .dayLow(4020d)
                .dayHigh(4111d).build());

        SRMatrix s15Min = sRMatrixService.addSRMatrix(SRMatrix.builder().active(true)
                .instrument(gold)
                .resistance(4056d)
                .support(4028d)
                .timeFrame(15)
                .timeFrameUnit(TimeFramesUnit.MINUTE.getUnit())
                .build());

        SRMatrix s4Hour = sRMatrixService.addSRMatrix(SRMatrix.builder().active(true)
                .instrument(gold)
                .resistance(4105d)
                .support(4028d)
                .timeFrame(4)
                .timeFrameUnit(TimeFramesUnit.HOUR.getUnit())
                .build());

        Configuration configuration = configurationService.addConfiguration(Configuration.builder()
                .lev(20)
                .maxPercentAllowedPerInstrument(2d)
                .percentAllocationAllowed(15d)
                .noOfInsutrments(1)
                .build());

        positionService.add(Position.builder()
                .currentPositionEquity(50d)
                .leverage(20)
                .instrument(gold)
                .srMatrix(s15Min)
                .strategy(strategy)
                .active(true)
                .marketConditions(marketConditions)
                .configuration(configuration).build());

        positionService.add(Position.builder()
                .currentPositionEquity(250d)
                .leverage(20)
                .instrument(gold)
                .srMatrix(s4Hour)
                .strategy(strategy)
                .active(true)
                .marketConditions(marketConditions)
                .configuration(configuration)
                .build());

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
            String portfolValue = restClient.get()
                    .uri("http://localhost:8081/etoro/portfolio-value")
                    .retrieve().body(String.class);
            System.out.println(portfolValue);
        }

    }
}
