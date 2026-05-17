package com.hkcapital.portflio.service.api.etoro.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hkcapital.portflio.broker.etoro.config.EtoroApiConfiguration;
import com.hkcapital.portflio.market.structure.MarketStructureManagerCache;
import com.hkcapital.portflio.service.api.etoro.websocket.LiveResponseMapper;
import com.hkcapital.portflio.service.candle.etoro.EtoroCandleService;
import com.hkcapital.portflio.service.candle.etoro.impl.EtoroLiveFeedListener;
import com.hkcapital.portflio.service.instrument.InstrumentService;
import com.hkcapital.portflio.service.marketfeed.observer.MarketFeedObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.http.HttpClient;

public class StartWebSocketRunner implements Runnable
{
    private final Logger logger = LoggerFactory.getLogger(StartWebSocketRunner.class);
    //public static final String ETORO_WEB_SOCKET_URL = "wss://ws.etoro.com/ws";

    public static final String ETORO_WEB_SOCKET_URL =  "ws://localhost:8025/ws/etoro";
    private final EtoroApiConfiguration etoroApiConfiguration;
    private final MarketFeedObserver marketFeedObserver;
    private final LiveResponseMapper liveResponseMapper;
    private final InstrumentService instrumentService;
    private final EtoroCandleService etoroCandleService;

    private final MarketStructureManagerCache marketStructureManagerCache;
    private final ObjectMapper objectMapper;

    public StartWebSocketRunner(EtoroApiConfiguration etoroApiConfiguration,
                                MarketFeedObserver marketFeedObserver,
                                LiveResponseMapper liveResponseMapper,
                                InstrumentService instrumentService,
                                ObjectMapper objectMapper,
                                EtoroCandleService etoroCandleService,
                                MarketStructureManagerCache marketStructureManagerCache)
    {
        this.etoroApiConfiguration = etoroApiConfiguration;
        this.marketFeedObserver = marketFeedObserver;
        this.liveResponseMapper = liveResponseMapper;
        this.instrumentService = instrumentService;
        this.objectMapper = objectMapper;
        this.etoroCandleService = etoroCandleService;
        this.marketStructureManagerCache = marketStructureManagerCache;
    }

    @Override
    public void run()
    {

        logger.info("Connected to URL [{}]", ETORO_WEB_SOCKET_URL);
        HttpClient.newHttpClient().newWebSocketBuilder()
                .buildAsync(
                        URI.create(ETORO_WEB_SOCKET_URL),
                        new EtoroLiveFeedListener(etoroApiConfiguration, marketFeedObserver,
                                liveResponseMapper, instrumentService, objectMapper, etoroCandleService,
                                marketStructureManagerCache))
                .join();
    }
}
