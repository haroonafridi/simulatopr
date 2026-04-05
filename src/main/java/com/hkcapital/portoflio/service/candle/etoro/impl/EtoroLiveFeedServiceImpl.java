package com.hkcapital.portoflio.service.candle.etoro.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hkcapital.portoflio.broker.etoro.config.EtoroApiConfiguration;
import com.hkcapital.portoflio.service.api.etoro.websocket.LiveInstrumentRate;
import com.hkcapital.portoflio.model.Instrument;
import com.hkcapital.portoflio.service.instrument.InstrumentService;
import com.hkcapital.portoflio.service.marketfeed.observer.MarketFeedObserver;
import com.hkcapital.portoflio.service.helper.LiveResponseMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.net.http.WebSocket;
import java.net.http.WebSocket.Listener;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

@Component
public class EtoroLiveFeedServiceImpl implements Listener
{
    private final Logger logger = LoggerFactory.getLogger(EtoroLiveFeedServiceImpl.class);
    private final EtoroApiConfiguration apiConfiguration;
    private final MarketFeedObserver marketFeedObserver;
    private final LiveResponseMapper liveResponseMapper;
    private final InstrumentService instrumentService;
    private final ObjectMapper objectMapper;
    private final Set<String> subscribedTopics = new HashSet<>();
    public EtoroLiveFeedServiceImpl(EtoroApiConfiguration apiConfiguration,
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
        performAuth(webSocket, apiConfiguration);
        webSocket.request(1);
    }

    @Override
    public CompletionStage<?> onText(WebSocket ws, CharSequence data, boolean last)
    {
        JsonNode node = null;
        try
        {
            node = objectMapper.readTree(data.toString());
            if (node.has("operation") &&
                    "Authenticate".equals(node.get("operation").asText()) &&
                    node.path("success").asBoolean(false))
            {

                logger.info("Authentication successful");

                if (instrumentService != null)
                {
                    List<Instrument> instrumentList = instrumentService.findAll()//
                            .stream()//
                            .filter(instrument -> instrument != null && instrument.getActive()) //
                            .collect(Collectors.toList());

                    instrumentList.stream().forEach(instrument ->
                    {
                        if (instrument != null && instrument.getActive() && //
                                instrument.getEtoroInstrumentId() != null)
                        {
                            subscribeInstrument(ws, "" + instrument.getEtoroInstrumentId());
                        }
                    });
                }
            }
        } catch (JsonProcessingException e)
        {
            throw new RuntimeException(e);
        }
        LiveInstrumentRate liveInstrumentRate = liveResponseMapper.mapResponse(data.toString());
        marketFeedObserver.process(liveInstrumentRate);
        ws.request(1);
        return null;
    }

    @Override
    public void onError(WebSocket webSocket, Throwable error)
    {
        System.err.println("WebSocket error: " + error);
    }

    @Override
    public CompletionStage<?> onClose(WebSocket webSocket, int statusCode, String reason)
    {
        System.out.println("WebSocket closed: " + reason);
        return null;
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
        logger.error("Authentication sent!!");
    }


    private void subscribeInstrument(WebSocket webSocket, String instrumentId)
    {
        if (subscribedTopics.contains(instrumentId))
        {
            logger.error("Already subscribed to  [{}]", instrumentId);
            return;
        }
        // For testing, use snapshot = true to get immediate data
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
        logger.info("Subscribe request sent for instrument:" + instrumentId);
    }

}
