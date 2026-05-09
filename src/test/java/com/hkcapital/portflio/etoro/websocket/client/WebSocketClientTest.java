package com.hkcapital.portflio.etoro.websocket.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hkcapital.portflio.broker.etoro.config.EtoroApiConfiguration;
import com.hkcapital.portflio.service.api.etoro.websocket.LiveResponseMapper;
import com.hkcapital.portflio.service.candle.etoro.EtoroCandleService;
import com.hkcapital.portflio.service.candle.etoro.impl.EtoroLiveFeedListener;
import com.hkcapital.portflio.service.instrument.InstrumentService;
import com.hkcapital.portflio.service.marketfeed.observer.MarketFeedObserver;
import com.hkcapital.portflio.service.marketfeed.subscriber.impl.MarketFeedDbWriterSub;
import org.glassfish.tyrus.server.Server;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.WebSocket;
import java.util.UUID;

@SpringBootTest
public class WebSocketClientTest
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

    @AfterAll
    static void stopServer() throws InterruptedException
    {
        Thread.sleep(20000);
        server.stop();
    }

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
                        URI.create("ws://localhost:8025/ws/etoro"),
                        etoroLiveFeedService)
                .join();
        etoroLiveFeedService.subscribeInstrument(ws, "18");
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
                        URI.create("ws://localhost:8025/ws/etoro"),
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
