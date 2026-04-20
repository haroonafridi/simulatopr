package com.hkcapital.portoflio.service.api.etoro.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hkcapital.portoflio.broker.etoro.config.EtoroApiConfiguration;
import com.hkcapital.portoflio.service.api.etoro.websocket.LiveResponseMapper;
import com.hkcapital.portoflio.service.candle.etoro.EtoroCandleService;
import com.hkcapital.portoflio.service.candle.etoro.impl.EtoroLiveFeedListener;
import com.hkcapital.portoflio.service.instrument.InstrumentService;
import com.hkcapital.portoflio.service.marketfeed.observer.MarketFeedObserver;

import java.net.URI;
import java.net.http.HttpClient;

public class StartWebSocketRunner implements Runnable
{
    private static final String ETORO_WEB_SOCKET_URL = "wss://ws.etoro.com/ws";
    private final EtoroApiConfiguration etoroApiConfiguration;
    private final MarketFeedObserver marketFeedObserver;
    private final LiveResponseMapper liveResponseMapper;
    private final InstrumentService instrumentService;
    private final EtoroCandleService etoroCandleService;
    private final ObjectMapper objectMapper;

    public StartWebSocketRunner(EtoroApiConfiguration etoroApiConfiguration,
                                MarketFeedObserver marketFeedObserver,
                                LiveResponseMapper liveResponseMapper,
                                InstrumentService instrumentService,
                                ObjectMapper objectMapper,
                                EtoroCandleService etoroCandleService)
    {
        this.etoroApiConfiguration = etoroApiConfiguration;
        this.marketFeedObserver = marketFeedObserver;
        this.liveResponseMapper = liveResponseMapper;
        this.instrumentService = instrumentService;
        this.objectMapper = objectMapper;
        this.etoroCandleService = etoroCandleService;
    }

    @Override
    public void run()
    {
        HttpClient.newHttpClient().newWebSocketBuilder()
                .buildAsync(
                        URI.create(ETORO_WEB_SOCKET_URL),
                        new EtoroLiveFeedListener(etoroApiConfiguration, marketFeedObserver,
                                liveResponseMapper, instrumentService, objectMapper, etoroCandleService))
                .join();
    }
}
