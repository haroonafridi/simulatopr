package com.hkcapital.portoflio.etoro.websocket;

import com.hkcapital.portoflio.etoro.apiinformation.EtoroAPIInformationService;
import com.hkcapital.portoflio.etoro.apiinformation.EtoroAPIInformationDemoServiceImpl;
import com.hkcapital.portoflio.etoro.master.Instruments;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.WebSocket;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.CountDownLatch;

public class EToroWSClient implements WebSocket.Listener //
{
    private static final Logger logger = LoggerFactory.getLogger(EToroWSClient.class.getName());
    private final Set<String> subscribedTopics = new HashSet<>();
    private final CountDownLatch latch = new CountDownLatch(1);
    private final ObjectMapper objectMapper = new ObjectMapper();

    public static void main(String[] args) throws InterruptedException {
        EtoroAPIInformationService apiInformation = new EtoroAPIInformationDemoServiceImpl();
        new EToroWSClient().start(apiInformation);
    }

    public void start(EtoroAPIInformationService apiInformation) throws InterruptedException //
    {
        HttpClient.newHttpClient()
                .newWebSocketBuilder()
                .buildAsync(URI.create("wss://ws.etoro.com/ws"), this)
                .thenAccept(ws -> {
                    logger.info("Connection request sent to etoro websocket!");
                    performAuth(ws,apiInformation);
                });
        latch.await(); // Keep the program alive
    }

    private void performAuth(WebSocket ws, EtoroAPIInformationService apiInformation) {
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
                                     boolean last) {
        String message = data.toString();
        logger.info("Message recieved [{}]", message);
        try {
            JsonNode node = objectMapper.readTree(message);

            // 1️⃣ Check if auth succeeded
            if (node.has("operation") &&
                    "Authenticate".equals(node.get("operation").asText()) &&
                    node.path("success").asBoolean(false)) {

                System.out.println("Authentication successful");
                subscribeInstrument(webSocket, Instruments.BTC.getInstrumentId().toString());
                //subscribeInstrument(webSocket, Instruments.GOLD.getInstrumentId().toString());
            }

            if (node.has("operation") &&
                    "Subscribe".equals(node.get("operation").asText())) {

                if (node.path("success").asBoolean(false)) {
                    System.out.println("Subscription successful for topics: " +
                            node.path("data").path("topics").toString());
                } else {
                    String code = node.path("errorCode").asText();
                    if ("TopicAlreadySubscribed".equals(code)) {
                        System.out.println("Already subscribed — skipping");
                    } else {
                        System.err.println("Subscription error: " + node.path("errorMessage").asText());
                    }
                }
            }
            if (node.has("topic") && node.get("topic").asText().startsWith("instrument:")) {
                String topic = node.get("topic").asText();
                JsonNode tickData = node.get("data");
                System.out.println("Tick for " + topic + " → " + tickData.toString());
            }

        } catch (Exception e) {
           throw new RuntimeException(e.getMessage());
        }
        return WebSocket.Listener.super.onText(webSocket, data, last);
    }
    @Override
    public void onError(WebSocket webSocket, Throwable error) {
        logger.error("Error recieved [{}]", error.getMessage());
        error.printStackTrace();
    }
    @Override
    public CompletionStage<?> onClose(WebSocket webSocket,
                                      int statusCode,
                                      String reason) {
        logger.error("Disconnected  [{}]", reason);
        return WebSocket.Listener.super.onClose(webSocket, statusCode, reason);
    }

    private void subscribeInstrument(WebSocket webSocket, String instrumentId) {
        if (subscribedTopics.contains(instrumentId)) {
            System.out.println("Already subscribed to " + instrumentId);
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
