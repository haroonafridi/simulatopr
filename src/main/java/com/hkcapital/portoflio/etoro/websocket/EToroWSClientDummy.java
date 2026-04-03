package com.hkcapital.portoflio.etoro.websocket;

import com.hkcapital.portoflio.config.EtoroApiConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.WebSocket;
import java.util.UUID;
import java.util.concurrent.CompletionStage;


@Component
public class EToroWSClientDummy implements WebSocket.Listener //
{
    private static final Logger logger = LoggerFactory.getLogger(EToroWSClientDummy.class.getName());

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

    }

}
