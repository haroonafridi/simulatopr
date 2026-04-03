package com.hkcapital.portoflio.etoro.websocket.client;

import com.hkcapital.portoflio.config.EtoroApiConfiguration;

import java.net.http.WebSocket;
import java.net.http.WebSocket.Listener;
import java.util.UUID;
import java.util.concurrent.CompletionStage;

public class EtoroLiveFeedListener implements Listener
{
    private final EtoroApiConfiguration apiConfiguration;

    public EtoroLiveFeedListener(final EtoroApiConfiguration apiConfiguration)
    {
        this.apiConfiguration = apiConfiguration;
    }

    @Override
    public void onOpen(WebSocket webSocket)
    {
        String id = UUID.randomUUID().toString();
        String authJson = String.format(
                "{ \"id\": \"%s\", \"data\": { \"userKey\": \"%s\", \"apiKey\": \"%s\" } }",
                id,
                apiConfiguration.getUserKey(),
                apiConfiguration.getApiKey()
        );
        webSocket.sendText(authJson, true);
        webSocket.request(1);
    }

    @Override
    public CompletionStage<?> onText(WebSocket ws, CharSequence data, boolean last)
    {
        System.out.println(data);
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

}
