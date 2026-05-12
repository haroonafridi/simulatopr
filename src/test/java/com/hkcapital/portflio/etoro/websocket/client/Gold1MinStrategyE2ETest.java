package com.hkcapital.portflio.etoro.websocket.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hkcapital.portflio.broker.etoro.config.EtoroApiConfiguration;
import com.hkcapital.portflio.broker.etoro.config.TradingConfiguration;
import com.hkcapital.portflio.market.indicators.Unit;
import com.hkcapital.portflio.model.*;
import com.hkcapital.portflio.service.api.etoro.impl.StartWebSocketRunner;
import com.hkcapital.portflio.service.api.etoro.websocket.LiveResponseMapper;
import com.hkcapital.portflio.service.candle.etoro.EtoroCandleService;
import com.hkcapital.portflio.service.candle.etoro.impl.EtoroLiveFeedListener;
import com.hkcapital.portflio.service.configuration.ConfigurationService;
import com.hkcapital.portflio.service.instrument.InstrumentService;
import com.hkcapital.portflio.service.marketconditions.MarketConditionsService;
import com.hkcapital.portflio.service.marketfeed.observer.MarketFeedObserver;
import com.hkcapital.portflio.service.marketfeed.subscriber.impl.MarketFeedDbWriterSub;
import com.hkcapital.portflio.service.orders.OrderManagerService;
import com.hkcapital.portflio.service.srmatrix.SRMatrixService;
import com.hkcapital.portflio.service.strategy.StrategyService;
import org.glassfish.tyrus.server.Server;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.WebSocket;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
public class Gold1MinStrategyE2ETest
{
    private static Server server;
    @Autowired
    private EtoroApiConfiguration etoroApiConfiguration;

    @Autowired
    private StrategyService strategyService;

    @Autowired
    private LiveResponseMapper liveResponseMapper;

    @Autowired
    private MarketFeedObserver marketFeedObserver;

    @Autowired
    private MarketFeedDbWriterSub marketFeedDbWriter;

    @Autowired
    private InstrumentService instrumentService;
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private EtoroCandleService etoroCandleService;

    @Autowired
    private ConfigurationService configurationService;

    @Autowired
    private MarketConditionsService marketConditionsService;
    @Autowired
    private SRMatrixService srMatrixService;

    @Autowired
    private OrderManagerService orderManagerService;



    CountDownLatch done = new CountDownLatch(1);

    @Test
    void shouldTestGold1MinStrategy() throws InterruptedException
    {
        strategyService.removeAll();
        marketConditionsService.removeAll();
        configurationService.removeAll();
        srMatrixService.removeAll();
        instrumentService.removeAll();
        etoroCandleService.removeAll();

        instrumentService.addInstrument(Instrument.builder().etoroInstrumentId(18)
                .instrumentDesc("Gold")
                .instrumentTicker("GOLD")
                .name("GOLD")
                .active(true)
                .maxSlippage(1.75d)
                .build());

        Instrument gold = instrumentService.findAll().stream().findFirst().get();

        configurationService.addConfiguration(Configuration.builder().lev(20)
                .noOfInsutrments(1)
                .noOfPositionsPerInstruments(3)
                .percentAllocationAllowed(15d)
                .maxPercentAllowedPerInstrument(7.5d)
                .build());

        Configuration goldConfig = configurationService.findAll().stream().findFirst().get();

        marketConditionsService.addMarketCondition(MarketConditions.builder()
                .instrument(gold)
                .percentMove(2d)
                .dayHigh(4500d)
                .dayLow(4400d)
                .build());

        MarketConditions goldMarketConditions = marketConditionsService.findAll().stream().findFirst().get();

        srMatrixService.addSRMatrix(SRMatrix.builder()
                .active(true)
                .resistance(4500d)
                .support(4400d)
                .timeFrameUnit(Unit.MINUTE.getUnit())
                .timeFrame(1)
                .instrument(gold).build());

        srMatrixService.addSRMatrix(SRMatrix.builder()
                .active(true)
                .resistance(4500d)
                .support(4400d)
                .timeFrameUnit(Unit.MINUTE.getUnit())
                .timeFrame(15)
                .instrument(gold).build());


        srMatrixService.addSRMatrix(SRMatrix.builder()
                .active(true)
                .resistance(4838d)
                .support(4673d)
                .timeFrameUnit(Unit.HOUR.getUnit())
                .timeFrame(1)
                .instrument(gold).build());

        srMatrixService.addSRMatrix(SRMatrix.builder()
                .active(true)
                .resistance(4862d)
                .support(4576d)
                .timeFrameUnit(Unit.HOUR.getUnit())
                .timeFrame(4)
                .instrument(gold).build());

        SRMatrix srMatrix1Mins  = getSrMatrix(Unit.MINUTE.getUnit(), 1);
        SRMatrix srMatrix15Mins  = getSrMatrix(Unit.MINUTE.getUnit(), 15);
        SRMatrix srMatrix1Hour  = getSrMatrix(Unit.HOUR.getUnit(), 1);
        SRMatrix srMatrix4Hour  = getSrMatrix(Unit.HOUR.getUnit(), 4);

        Strategy strategy = Strategy.builder()
                .description("Test Gold Strategy")
                .creationDate(LocalDateTime.now())
                .capitalAllocated(1500d)
                .active(true).
                build();

        Position pos1Min = Position.builder()
                .instrument(gold)
                .leverage(20)
                .allowedFirePower(null)
                .capitalRemainingFirePower(null)
                .configuration(goldConfig)
                .currentPositionEquity(null)
                .marketConditions(goldMarketConditions)
                .currentPositionEquity(50d)
                .portfolioValue(null)
                .srMatrix(srMatrix1Mins)
                .strategy(strategy)
                .tradingSessions(null)
                .percentCapitalDeployed(null)
                .remainingFirepower(null)
                .stopLoss(10d)
                .takeProfit(10d)
                .build();

        Position pos15Min = Position.builder()
                .instrument(gold)
                .leverage(20)
                .allowedFirePower(null)
                .capitalRemainingFirePower(null)
                .configuration(goldConfig)
                .currentPositionEquity(null)
                .marketConditions(goldMarketConditions)
                .currentPositionEquity(100d)
                .portfolioValue(null)
                .srMatrix(srMatrix15Mins)
                .strategy(strategy)
                .tradingSessions(null)
                .percentCapitalDeployed(null)
                .remainingFirepower(null)
                .stopLoss(10d)
                .takeProfit(10d)
                .build();


        Position pos1Hour = Position.builder()
                .instrument(gold)
                .leverage(20)
                .allowedFirePower(null)
                .capitalRemainingFirePower(null)
                .configuration(goldConfig)
                .currentPositionEquity(null)
                .marketConditions(goldMarketConditions)
                .currentPositionEquity(250d)
                .portfolioValue(null)
                .srMatrix(srMatrix1Hour)
                .strategy(strategy)
                .tradingSessions(null)
                .percentCapitalDeployed(null)
                .remainingFirepower(null)
                .stopLoss(10d)
                .takeProfit(30d)
                .build();

        Position pos4Hour = Position.builder()
                .instrument(gold)
                .leverage(20)
                .allowedFirePower(null)
                .capitalRemainingFirePower(null)
                .configuration(goldConfig)
                .currentPositionEquity(null)
                .marketConditions(goldMarketConditions)
                .currentPositionEquity(500d)
                .portfolioValue(null)
                .srMatrix(srMatrix4Hour)
                .strategy(strategy)
                .tradingSessions(null)
                .percentCapitalDeployed(null)
                .remainingFirepower(null)
                .stopLoss(10d)
                .takeProfit(30d)
                .build();

        strategy.setPositionPnLList(List.of(pos1Min, pos15Min, pos1Hour, pos4Hour));
        strategyService.addStrategy(strategy);
        TradingConfiguration.ACTIVATE_AUTOMATIC_TRADING = Boolean.TRUE;
        marketFeedObserver.addMarketFeedSubscriber(marketFeedDbWriter);
        HttpClient client = HttpClient.newHttpClient();
        EtoroLiveFeedListener etoroLiveFeedService = new EtoroLiveFeedListener(etoroApiConfiguration, //
                marketFeedObserver, //
                liveResponseMapper, //
                instrumentService, //
                objectMapper,
                etoroCandleService,
                null); // add market cach here
        WebSocket ws = client.newWebSocketBuilder()
                .buildAsync(
                        URI.create(StartWebSocketRunner.ETORO_WEB_SOCKET_URL),
                        etoroLiveFeedService)
                .join();
        etoroLiveFeedService.subscribeInstrument(ws, "18");
        Thread.sleep(10000 * 16000);
    }

    private SRMatrix getSrMatrix(String unit, Integer interval)
    {
        return srMatrixService.findAll().stream().filter(matrix ->
                matrix.getTimeFrameUnit().equals(unit) &&
                        matrix.getTimeFrame().compareTo(interval) == 0).findFirst().get();
    }

    @Test
    void shouldReceivePing()
    {
        marketFeedObserver.addMarketFeedSubscriber(marketFeedDbWriter);
        HttpClient client = HttpClient.newHttpClient();
        EtoroLiveFeedListener etoroLiveFeedService = new EtoroLiveFeedListener(etoroApiConfiguration, //
                marketFeedObserver, //
                liveResponseMapper, //
                instrumentService, //
                objectMapper,
                etoroCandleService,
                null); //add market cache here
        WebSocket ws = client.newWebSocketBuilder()
                .buildAsync(
                        URI.create(StartWebSocketRunner.ETORO_WEB_SOCKET_URL),
                        etoroLiveFeedService)
                .join();
        ws.sendText("ping", true);
    }


    @Test
    void shouldReconnect()
    {
        marketFeedObserver.addMarketFeedSubscriber(marketFeedDbWriter);
        HttpClient client = HttpClient.newHttpClient();
        EtoroLiveFeedListener etoroLiveFeedService = new EtoroLiveFeedListener(etoroApiConfiguration, //
                marketFeedObserver, //
                liveResponseMapper, //
                instrumentService, //
                objectMapper,
                etoroCandleService, null);  //add market cache here
        WebSocket ws = client.newWebSocketBuilder()
                .buildAsync(
                        URI.create(StartWebSocketRunner.ETORO_WEB_SOCKET_URL),
                        etoroLiveFeedService)
                .join();
        ws.sendText("ping", true);
    }

    private String getAuthInfo(EtoroApiConfiguration apiInformation)
    {
        return """
                {
                  "id": "%s",
                  "operation": "Authenticate",
                  "data": {
                    "userKey": "%s",
                    "apiKey": "%s"
                  }
                }
                """.formatted(
                UUID.randomUUID(),
                apiInformation.getUserKey(),
                apiInformation.getApiKey()
        );
    }

}
