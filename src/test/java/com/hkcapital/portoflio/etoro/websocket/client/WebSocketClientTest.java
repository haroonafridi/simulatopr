package com.hkcapital.portoflio.etoro.websocket.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hkcapital.portoflio.broker.etoro.config.EtoroApiConfiguration;
import com.hkcapital.portoflio.service.candle.etoro.impl.EtoroLiveFeedServiceImpl;
import com.hkcapital.portoflio.etoro.websocket.server.EtoroTestServer;
import com.hkcapital.portoflio.service.instrument.InstrumentService;
import com.hkcapital.portoflio.service.marketfeed.observer.MarketFeedObserver;
import com.hkcapital.portoflio.service.api.etoro.websocket.LiveResponseMapper;
import com.hkcapital.portoflio.service.marketfeed.subscriber.impl.MarketFeedDbWriterSub;
import org.glassfish.tyrus.server.Server;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
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

    @BeforeAll
    static void startServer() throws Exception
    {

        server = new Server(
                "localhost",
                8025,
                "/ws",
                null,
                EtoroTestServer.class
        );

        server.start();
    }

    @AfterAll
    static void stopServer() throws InterruptedException
    {
        Thread.sleep(20000);
        server.stop();
    }

    @Test
    void testClientConnection() throws InterruptedException
    {
        marketFeedObserver.addMarketFeedSubscriber(marketFeedDbWriter);
        HttpClient client = HttpClient.newHttpClient();
        WebSocket ws = client.newWebSocketBuilder()
                .buildAsync(
                        URI.create("ws://localhost:8025/ws/etoro"),
                        new EtoroLiveFeedServiceImpl(etoroApiConfiguration, marketFeedObserver,
                                liveResponseMapper,instrumentService , objectMapper ))
                .join();
        ws.sendText("{\n" +
                "  \"id\": \"ed72693c-1545-4fa1-8a10-aca7cf5419a6\",\n" +
                "  \"operation\": \"Subscribe\",\n" +
                "  \"data\": {\n" +
                "    \"topics\": [\n" +
                "        \"instrument:28\",\n" +
                "        \"instrument:18\"\n" +
                "    ],\n" +
                "    \"snapshot\": true\n" +
                "  }\n" +
                "}", true);
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
