package com.hkcapital.portoflio.etoro.websocket.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hkcapital.portoflio.broker.etoro.config.EtoroApiConfiguration;
import com.hkcapital.portoflio.service.api.etoro.impl.StartWebSocketRunner;
import com.hkcapital.portoflio.service.api.etoro.websocket.LiveResponseMapper;
import com.hkcapital.portoflio.service.candle.etoro.EtoroCandleService;
import com.hkcapital.portoflio.service.candle.etoro.impl.EtoroLiveFeedListener;
import com.hkcapital.portoflio.service.instrument.InstrumentService;
import com.hkcapital.portoflio.service.marketfeed.observer.MarketFeedObserver;
import com.hkcapital.portoflio.service.marketfeed.subscriber.impl.MarketFeedDbWriterSub;
import org.glassfish.tyrus.server.Server;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.WebSocket;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;

@SpringBootTest
public class WebSocketGoldClientTest
{
    private static Server server;
    @Autowired
    private EtoroApiConfiguration etoroApiConfiguration;

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

    CountDownLatch done = new CountDownLatch(1);

    @Test
    void shouldEstablishConnection() throws InterruptedException
    {
        marketFeedObserver.addMarketFeedSubscriber(marketFeedDbWriter);
        HttpClient client = HttpClient.newHttpClient();
        EtoroLiveFeedListener etoroLiveFeedService = new EtoroLiveFeedListener(etoroApiConfiguration, //
                marketFeedObserver, //
                liveResponseMapper, //
                instrumentService, //
                objectMapper,
                etoroCandleService);
        WebSocket ws = client.newWebSocketBuilder()
                .buildAsync(
                        URI.create(StartWebSocketRunner.ETORO_WEB_SOCKET_URL),
                        etoroLiveFeedService)
                .join();
        etoroLiveFeedService.subscribeInstrument(ws, "18");
        Thread.sleep(10000 * 16000);
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
                etoroCandleService);
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
                etoroCandleService);
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
