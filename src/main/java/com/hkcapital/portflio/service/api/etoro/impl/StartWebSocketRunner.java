package com.hkcapital.portflio.service.api.etoro.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hkcapital.portflio.broker.etoro.config.EtoroApiConfiguration;
import com.hkcapital.portflio.market.structure.MarketStructureCache;
import com.hkcapital.portflio.repository.registry.ServiceRegistery;
import com.hkcapital.portflio.service.api.etoro.websocket.LiveResponseMapper;
import com.hkcapital.portflio.service.bandlogger.Bandlogger;
import com.hkcapital.portflio.service.candle.etoro.EtoroCandleService;
import com.hkcapital.portflio.service.candle.etoro.impl.EtoroLiveFeedListener;
import com.hkcapital.portflio.service.env.EnvService;
import com.hkcapital.portflio.service.instrument.InstrumentService;
import com.hkcapital.portflio.service.marketfeed.observer.MarketFeedObserver;
import com.hkcapital.portflio.service.registry.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.http.HttpClient;

public class StartWebSocketRunner implements Runnable
{
    private final Logger logger = LoggerFactory.getLogger(StartWebSocketRunner.class);
    private final EtoroApiConfiguration etoroApiConfiguration;
    private final MarketFeedObserver marketFeedObserver;
    private final LiveResponseMapper liveResponseMapper;
    private final InstrumentService instrumentService;
    private final EtoroCandleService etoroCandleService;

    private final MarketStructureCache marketStructureManagerCache;
    private final ObjectMapper objectMapper;
    private final Bandlogger bandlogger;

    private final EnvService envService;

    private final ServiceRegistery<Service> serviceRegistery;
    public StartWebSocketRunner(EtoroApiConfiguration etoroApiConfiguration,
                                MarketFeedObserver marketFeedObserver,
                                LiveResponseMapper liveResponseMapper,
                                InstrumentService instrumentService,
                                ObjectMapper objectMapper,
                                EtoroCandleService etoroCandleService,
                                MarketStructureCache marketStructureManagerCache,
                                Bandlogger bandlogger,
                                EnvService envService,
                                ServiceRegistery serviceRegistery)
    {
        this.etoroApiConfiguration = etoroApiConfiguration;
        this.marketFeedObserver = marketFeedObserver;
        this.liveResponseMapper = liveResponseMapper;
        this.instrumentService = instrumentService;
        this.objectMapper = objectMapper;
        this.etoroCandleService = etoroCandleService;
        this.marketStructureManagerCache = marketStructureManagerCache;
        this.bandlogger = bandlogger;
        this.envService = envService;
        this.serviceRegistery = serviceRegistery;
    }

    @Override
    public void run()
    {
        logger.info("Connected to URL [{}]", etoroApiConfiguration.getUrl());
        HttpClient.newHttpClient().newWebSocketBuilder()
                .buildAsync(
                        URI.create(etoroApiConfiguration.getUrl()),
                        new EtoroLiveFeedListener(etoroApiConfiguration, marketFeedObserver,
                                liveResponseMapper, instrumentService, objectMapper, etoroCandleService,
                                marketStructureManagerCache, bandlogger, envService, serviceRegistery))
                .join();
    }
}
