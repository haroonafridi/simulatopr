package com.hkcapital.portoflio.service.candle.etoro.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hkcapital.portoflio.broker.etoro.config.EtoroApiConfiguration;
import com.hkcapital.portoflio.model.Instrument;
import com.hkcapital.portoflio.service.api.etoro.websocket.LiveInstrumentRate;
import com.hkcapital.portoflio.service.api.etoro.websocket.LiveResponseMapper;
import com.hkcapital.portoflio.service.instrument.InstrumentService;
import com.hkcapital.portoflio.service.marketfeed.observer.MarketFeedObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.WebSocket;
import java.net.http.WebSocket.Listener;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.*;
import java.util.stream.Collectors;

@Component
public class EtoroLiveFeedListener implements Listener
{

    private final Logger logger = LoggerFactory.getLogger(EtoroLiveFeedListener.class);

    private final EtoroApiConfiguration apiConfiguration;
    private final MarketFeedObserver marketFeedObserver;
    private final LiveResponseMapper liveResponseMapper;
    private final InstrumentService instrumentService;
    private final ObjectMapper objectMapper;

    private final Set<String> subscribedTopics = ConcurrentHashMap.newKeySet();

    private final HttpClient httpClient = HttpClient.newHttpClient();

    private final ScheduledExecutorService scheduler =
            Executors.newSingleThreadScheduledExecutor();

    private volatile WebSocket webSocket;

    private volatile boolean reconnecting = false;

    public EtoroLiveFeedListener(EtoroApiConfiguration apiConfiguration,
                                 MarketFeedObserver marketFeedObserver,
                                 LiveResponseMapper liveResponseMapper,
                                 InstrumentService instrumentService,
                                 ObjectMapper objectMapper)
    {
        this.apiConfiguration = apiConfiguration;
        this.marketFeedObserver = marketFeedObserver;
        this.liveResponseMapper = liveResponseMapper;
        this.instrumentService = instrumentService;
        this.objectMapper = objectMapper;
    }

    @Override
    public void onOpen(WebSocket webSocket)
    {
        logger.info("WebSocket connected");
        this.webSocket = webSocket;
        reconnecting = false;
        subscribedTopics.clear();
        performAuth(webSocket, apiConfiguration);
        webSocket.request(1);
    }

    @Override
    public CompletionStage<?> onText(WebSocket ws, CharSequence data, boolean last)
    {
        try
        {
            JsonNode node = objectMapper.readTree(data.toString());

            if (node.has("operation") &&
                    "Authenticate".equals(node.get("operation").asText()) &&
                    node.path("success").asBoolean(false))
            {

                logger.info("Authentication successful");

                List<Instrument> instrumentList = instrumentService.findAll()
                        .stream()
                        .filter(instrument -> instrument != null && instrument.getActive())
                        .collect(Collectors.toList());

                instrumentList.forEach(instrument ->
                {
                    if (instrument.getEtoroInstrumentId() != null)
                    {
                        subscribeInstrument(ws,
                                String.valueOf(instrument.getEtoroInstrumentId()));
                    }
                });
            }
        }
        catch (JsonProcessingException e)
        {
            logger.error("JSON parse error", e);
        }

        logger.debug("Received tick [{}]", data.toString());

        LiveInstrumentRate liveInstrumentRate =
                liveResponseMapper.mapResponse(data.toString());

        marketFeedObserver.process(liveInstrumentRate);

        ws.request(1);

        return CompletableFuture.completedFuture(null);
    }

    @Override
    public void onError(WebSocket webSocket, Throwable error)
    {
        logger.error("WebSocket error", error);
        reconnect();
    }

    @Override
    public CompletionStage<?> onClose(WebSocket webSocket, int statusCode, String reason)
    {
        logger.warn("WebSocket closed [{}] {}", statusCode, reason);
        reconnect();
        return CompletableFuture.completedFuture(null);
    }

    private void performAuth(WebSocket ws, EtoroApiConfiguration apiInformation)
    {
        String authMessage = """
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

        ws.sendText(authMessage, true);

        logger.info("Authentication sent");
    }

    public void subscribeInstrument(WebSocket webSocket, String instrumentId)
    {
        if (subscribedTopics.contains(instrumentId))
        {
            return;
        }

        String subscribeMessage = """
                {
                  "id": "%s",
                  "operation": "Subscribe",
                  "data": {
                    "topics": ["instrument:%s"],
                    "snapshot": true
                  }
                }
                """.formatted(UUID.randomUUID(), instrumentId);

        webSocket.sendText(subscribeMessage, true);

        subscribedTopics.add(instrumentId);

        logger.info("Subscribed instrument {}", instrumentId);
    }

    private void reconnect()
    {
        if (reconnecting)
        {
            return;
        }

        reconnecting = true;

        logger.warn("Reconnecting to eToro WebSocket...");

        scheduler.schedule(() ->
        {
            httpClient.newWebSocketBuilder()
                    .buildAsync(URI.create("wss://ws.etoro.com/ws"), this)
                    .whenComplete((ws, error) ->
                    {
                        if (error != null)
                        {
                            logger.error("Reconnect failed", error);
                            reconnecting = false;
                            reconnect();
                        }
                    });

        }, 3, TimeUnit.SECONDS);
    }
}