package com.hkcapital.portoflio.etoro.websocket;

import com.hkcapital.portoflio.config.EtoroApiConfiguration;
import com.hkcapital.portoflio.service.MarketFeedObserver;
import com.hkcapital.portoflio.service.impl.LiveResponseMapper;
import org.springframework.stereotype.Component;

import java.net.http.WebSocket;
import java.net.http.WebSocket.Listener;
import java.util.UUID;
import java.util.concurrent.CompletionStage;

@Component
public class EtoroLiveFeedListener implements Listener
{
    private final EtoroApiConfiguration apiConfiguration;
    private final MarketFeedObserver marketFeedObserver;
    private final LiveResponseMapper liveResponseMapper;
    public EtoroLiveFeedListener(EtoroApiConfiguration apiConfiguration,
                                 MarketFeedObserver marketFeedObserver,
                                 LiveResponseMapper liveResponseMapper)
    {
        this.apiConfiguration = apiConfiguration;
        this.marketFeedObserver = marketFeedObserver;
        this.liveResponseMapper = liveResponseMapper;
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

}
