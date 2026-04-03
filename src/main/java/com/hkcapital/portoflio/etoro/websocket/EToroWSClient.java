package com.hkcapital.portoflio.etoro.websocket;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hkcapital.portoflio.config.EtoroApiConfiguration;
import com.hkcapital.portoflio.model.Instrument;
import com.hkcapital.portoflio.service.InstrumentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.WebSocket;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;


@Component
public class EToroWSClient implements WebSocket.Listener //
{
    private static final Logger logger = LoggerFactory.getLogger(EToroWSClient.class.getName());
    private final Set<String> subscribedTopics = new HashSet<>();
    private final CountDownLatch latch = new CountDownLatch(1);
    private final ObjectMapper objectMapper;
    private LivePriceResponseWrapper livePriceResponseWrapper;
    private LiveInstrumentRate liveInstrumentRate;
    private InstrumentService instrumentService;

    public EToroWSClient(final InstrumentService instrumentService, final ObjectMapper objectMapper)
    {
        this.instrumentService = instrumentService;
        this.objectMapper = objectMapper;
    }

    public void connect(EtoroApiConfiguration apiInformation, String url) throws InterruptedException //
    {
        HttpClient.newHttpClient()
                .newWebSocketBuilder()
                .buildAsync(URI.create(url), this)
                .thenAccept(ws ->
                {
                    logger.info("Connection request sent to etoro websocket!");
                    performAuth(ws, apiInformation);
                });
        latch.await();
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

    @Override
    public void onOpen(WebSocket webSocket)
    {
        logger.info("Connection successfully established!");
        WebSocket.Listener.super.onOpen(webSocket);
    }

    @Override
    public CompletionStage<?> onText(WebSocket webSocket,
                                     CharSequence data,
                                     boolean last)
    {
        String message = data.toString();
        logger.info("Started receiving data : {}", message);
        try
        {
            JsonNode node = objectMapper.readTree(message);

            // 1️⃣ Check if auth succeeded
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
                            subscribeInstrument(webSocket, "" + instrument.getEtoroInstrumentId());
                        }
                    });
                }
            }

            if (node.has("operation") &&
                    "Subscribe".equals(node.get("operation").asText()))
            {

                if (node.path("success").asBoolean(false))
                {
                    logger.info("Subscription successful for topics: " +
                            node.path("data").path("topics").toString());
                } else
                {
                    String code = node.path("errorCode").asText();
                    if ("TopicAlreadySubscribed".equals(code))
                    {
                        logger.info("Already subscribed — skipping");
                    } else
                    {
                        logger.info("Subscription error: " + node.path("errorMessage").asText());
                    }
                }
            }
            if (node.has("topic") && node.get("topic").asText().startsWith("instrument:"))
            {
                String topic = node.get("topic").asText();
                JsonNode tickData = node.get("data");
                logger.info("Tick for " + topic + " → " + tickData.toString());
            }

            livePriceResponseWrapper = objectMapper.readValue(message, LivePriceResponseWrapper.class);

            if (livePriceResponseWrapper.getMessages() != null && livePriceResponseWrapper.getMessages().size() > 0)
            {
                liveInstrumentRate = objectMapper.readValue(livePriceResponseWrapper.getMessages().get(0).getContent(), //
                        LiveInstrumentRate.class);
            }


        } catch (Exception e)
        {
            throw new RuntimeException(e.getMessage());
        }
        return WebSocket.Listener.super.onText(webSocket, data, last);
    }

    @Override
    public void onError(WebSocket webSocket, Throwable error)
    {
        logger.error("Error recieved [{}]", error.getMessage());
    }

    @Override
    public CompletionStage<?> onClose(WebSocket webSocket,
                                      int statusCode,
                                      String reason)
    {
        logger.error("Disconnected  [{}]", reason);
        return WebSocket.Listener.super.onClose(webSocket, statusCode, reason);
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

    public LivePriceResponseWrapper getLivePriceResponseWrapper()
    {
        return livePriceResponseWrapper;
    }

    public LiveInstrumentRate getLiveInstrumentRate()
    {
        return liveInstrumentRate;
    }
}
