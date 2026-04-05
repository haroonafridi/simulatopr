package com.hkcapital.portoflio.service.api.etoro.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hkcapital.portoflio.broker.etoro.config.EtoroApiConfiguration;
import com.hkcapital.portoflio.service.candle.etoro.impl.EtoroLiveFeedServiceImpl;
import com.hkcapital.portoflio.service.instrument.InstrumentService;
import com.hkcapital.portoflio.service.marketfeed.observer.MarketFeedObserver;
import com.hkcapital.portoflio.service.helper.LiveResponseMapper;

import java.net.URI;
import java.net.http.HttpClient;

public class StartWebSocketRunner implements Runnable
{
    private static final String ETORO_WEB_SOCKET_URL = "wss://ws.etoro.com/ws";
    private final EtoroApiConfiguration etoroApiConfiguration;
    private final MarketFeedObserver marketFeedObserver;
    private final LiveResponseMapper liveResponseMapper;

    private final InstrumentService instrumentService;
    private final ObjectMapper objectMapper;

    public StartWebSocketRunner(EtoroApiConfiguration etoroApiConfiguration,
                                MarketFeedObserver marketFeedObserver,
                                LiveResponseMapper liveResponseMapper,
                                InstrumentService instrumentService,
                                ObjectMapper objectMapper)
    {
        this.etoroApiConfiguration = etoroApiConfiguration;
        this.marketFeedObserver = marketFeedObserver;
        this.liveResponseMapper = liveResponseMapper;
        this.instrumentService = instrumentService;
        this.objectMapper = objectMapper;
    }

    @Override
    public void run()
    {
        HttpClient.newHttpClient().newWebSocketBuilder()
                .buildAsync(
                        URI.create(ETORO_WEB_SOCKET_URL),
                        new EtoroLiveFeedServiceImpl(etoroApiConfiguration, marketFeedObserver,
                                liveResponseMapper, instrumentService, objectMapper))
                .join();
    }
}
