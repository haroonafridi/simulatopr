package com.hkcapital.portflio.etoro.websocket.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hkcapital.portflio.broker.etoro.config.EtoroApiConfiguration;
import com.hkcapital.portflio.market.structure.MarketStructureCache;
import com.hkcapital.portflio.model.Instrument;
import com.hkcapital.portflio.service.api.etoro.EtoroApiService;
import com.hkcapital.portflio.service.api.etoro.websocket.LiveResponseMapper;
import com.hkcapital.portflio.service.bandlogger.Bandlogger;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.WebSocket;
import java.util.UUID;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
public abstract class EtoroWebSocketClientAbstract_IT
{
    protected static Server server;
    @Autowired
    protected EtoroApiConfiguration etoroApiConfiguration;
    @Autowired
    protected LiveResponseMapper liveResponseMapper;
    @Autowired
    protected MarketFeedObserver marketFeedObserver;
    @Autowired
    protected MarketFeedDbWriterSub marketFeedDbWriter;
    @Autowired
    protected InstrumentService instrumentService;
    @Autowired
    protected ObjectMapper objectMapper;
    @Autowired
    protected EtoroCandleService etoroCandleService;
    @Autowired
    protected MarketStructureCache marketStructureCache;
    @Autowired
    protected StrategyService strategyService;
    @Autowired
    protected ConfigurationService configurationService;
    @Autowired
    protected MarketConditionsService marketConditionsService;
    @Autowired
    protected SRMatrixService srMatrixService;
    @Autowired
    protected OrderManagerService orderManagerService;
    @Autowired
    protected Bandlogger bandlogger;

    private EtoroLiveFeedListener etoroLiveFeedService;

    @Autowired
    protected EtoroApiService etoroApiService;
    public WebSocket connect(Instrument instrument) throws InterruptedException
    {
        marketFeedObserver.addMarketFeedSubscriber(marketFeedDbWriter);
        HttpClient client = HttpClient.newHttpClient();
        etoroLiveFeedService = new EtoroLiveFeedListener(etoroApiConfiguration, //
                marketFeedObserver, //
                liveResponseMapper, //
                instrumentService, //
                objectMapper,
                etoroCandleService,
                marketStructureCache,
                bandlogger);  //add market cache here
        WebSocket ws = client.newWebSocketBuilder()
                .buildAsync(
                        URI.create("ws://localhost:8025/ws/etoro"),
                        etoroLiveFeedService)
                .join();
        etoroLiveFeedService.subscribeInstrument(ws, instrument.getEtoroInstrumentId().toString());
        return ws;
    }


    public void subscribeDataFeed(WebSocket ws, String date)
    {
        ws.sendText(date, true);
    }


    public EtoroLiveFeedListener getEtoroLiveFeedService(){
        return etoroLiveFeedService;
    }
    void shouldReconnect()
    {
        marketFeedObserver.addMarketFeedSubscriber(marketFeedDbWriter);
        HttpClient client = HttpClient.newHttpClient();
        EtoroLiveFeedListener etoroLiveFeedService = new EtoroLiveFeedListener(etoroApiConfiguration, //
                marketFeedObserver, //
                liveResponseMapper, //
                instrumentService, //
                objectMapper, //
                etoroCandleService, //
                marketStructureCache,
                bandlogger); // //add market cache here
        WebSocket ws = client.newWebSocketBuilder()
                .buildAsync(
                        URI.create("ws://localhost:8025/ws/etoro"),
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
                    "apiKey": "%s",
                    "date" : "01-01-2026"
                  }
                }
                """.formatted(
                UUID.randomUUID(),
                apiInformation.getUserKey(),
                apiInformation.getApiKey()
        );
    }

    public static Server getServer()
    {
        return server;
    }

    public EtoroApiConfiguration getEtoroApiConfiguration()
    {
        return etoroApiConfiguration;
    }

    public LiveResponseMapper getLiveResponseMapper()
    {
        return liveResponseMapper;
    }

    public MarketFeedObserver getMarketFeedObserver()
    {
        return marketFeedObserver;
    }

    public MarketFeedDbWriterSub getMarketFeedDbWriter()
    {
        return marketFeedDbWriter;
    }

    public InstrumentService getInstrumentService()
    {
        return instrumentService;
    }

    public ObjectMapper getObjectMapper()
    {
        return objectMapper;
    }

    public EtoroCandleService getEtoroCandleService()
    {
        return etoroCandleService;
    }

    public MarketStructureCache getMarketStructureCache()
    {
        return marketStructureCache;
    }

    public StrategyService getStrategyService()
    {
        return strategyService;
    }

    public ConfigurationService getConfigurationService()
    {
        return configurationService;
    }

    public MarketConditionsService getMarketConditionsService()
    {
        return marketConditionsService;
    }

    public SRMatrixService getSrMatrixService()
    {
        return srMatrixService;
    }

    public OrderManagerService getOrderManagerService()
    {
        return orderManagerService;
    }
}
