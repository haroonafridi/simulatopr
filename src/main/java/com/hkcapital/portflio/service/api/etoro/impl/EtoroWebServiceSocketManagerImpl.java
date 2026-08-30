package com.hkcapital.portflio.service.api.etoro.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hkcapital.portflio.broker.etoro.config.EtoroApiConfiguration;
import com.hkcapital.portflio.market.structure.MarketStructureCache;
import com.hkcapital.portflio.repository.registry.ServiceRegistery;
import com.hkcapital.portflio.service.api.etoro.websocket.LiveResponseMapper;
import com.hkcapital.portflio.service.bandlogger.Bandlogger;
import com.hkcapital.portflio.service.candle.etoro.EtoroCandleService;
import com.hkcapital.portflio.service.env.EnvService;
import com.hkcapital.portflio.service.instrument.InstrumentService;
import com.hkcapital.portflio.service.instrumentmarketstructureconf.InstrumentMarketStructureConfService;
import com.hkcapital.portflio.service.marketfeed.observer.MarketFeedObserver;
import com.hkcapital.portflio.service.marketfeed.subscriber.impl.BuySellSignalGeneratorSub;
import com.hkcapital.portflio.service.marketfeed.subscriber.impl.MarketFeedDbWriterSub;
import com.hkcapital.portflio.service.orders.OrderManagerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static java.util.concurrent.Executors.newSingleThreadScheduledExecutor;

@Service
public class EtoroWebServiceSocketManagerImpl implements com.hkcapital.portflio.service.api.etoro.EtoroWebSocketManagerService
{
    private final Logger logger = LoggerFactory.getLogger(EtoroWebServiceSocketManagerImpl.class);

    private final com.hkcapital.portflio.service.orders.OrderManagerService orderManagerService;
    private final com.hkcapital.portflio.service.instrument.InstrumentService instrumentService;

    private final EtoroApiConfiguration etoroApiConfiguration;
    private final ObjectMapper objectMapper;

    private final MarketFeedObserver marketFeedObserver;
    private final LiveResponseMapper liveResponseMapper;
    private final MarketFeedDbWriterSub marketFeedDbWriter;
    private final BuySellSignalGeneratorSub buySellManager;

    private final MarketStructureCache marketStructureManagerCache;

    private final InstrumentMarketStructureConfService instMrktStrCon;

    private final EtoroCandleService etoroCandleService;

    private final Bandlogger bandlogger;

    private final EnvService envService;

    private final ServiceRegistery serviceRegistery;

    public EtoroWebServiceSocketManagerImpl(final com.hkcapital.portflio.service.srmatrix.SRMatrixService srMatrixService, //
                                            final OrderManagerService orderManagerService, //
                                            final InstrumentService instrumentService, //
                                            final com.hkcapital.portflio.service.strategy.StrategyService strategyService, //
                                            final com.hkcapital.portflio.service.positions.PositionService positionService, //
                                            final EtoroApiConfiguration etoroApiConfiguration, //
                                            final ObjectMapper objectMapper, //
                                            final MarketFeedObserver marketFeedObserver, //
                                            final LiveResponseMapper liveResponseMapper,
                                            final MarketFeedDbWriterSub marketFeedDbWriter,
                                            final BuySellSignalGeneratorSub buySellManager,
                                            final EtoroCandleService etoroCandleService,
                                            final MarketStructureCache marketStructureManagerCache,
                                            final Bandlogger bandlogger,
                                            final EnvService envService,
                                            final InstrumentMarketStructureConfService instMrktStrCon,
                                            final ServiceRegistery serviceRegistery)
    {

        this.orderManagerService = orderManagerService;
        this.instrumentService = instrumentService;
        this.etoroApiConfiguration = etoroApiConfiguration;
        this.objectMapper = objectMapper;
        this.marketFeedObserver = marketFeedObserver;
        this.liveResponseMapper = liveResponseMapper;
        this.marketFeedDbWriter = marketFeedDbWriter;
        this.buySellManager = buySellManager;
        this.etoroCandleService = etoroCandleService;
        marketFeedObserver.addMarketFeedSubscriber(marketFeedDbWriter);
        marketFeedObserver.addMarketFeedSubscriber(buySellManager);
        marketFeedObserver.addMarketFeedSubscriber(orderManagerService);
        this.marketStructureManagerCache = marketStructureManagerCache;
        this.bandlogger = bandlogger;
        this.envService = envService;
        this.instMrktStrCon = instMrktStrCon;
        this.serviceRegistery = serviceRegistery;
    }

    @Override
    public void subscribeAndSchedule()
    {

        StartWebSocketRunner startWebSocket = //
                new StartWebSocketRunner(etoroApiConfiguration, marketFeedObserver, //
                        liveResponseMapper, instrumentService, objectMapper, etoroCandleService,
                        marketStructureManagerCache, bandlogger, envService,
                        instMrktStrCon, serviceRegistery);
        new Thread(startWebSocket).start();

        ScheduledExecutorService scheduler = newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(() ->
        {
            try
            {
                orderManagerService.fetchAndCloseEtoroOrder();
            } catch (Exception e)
            {
                logger.error("Error in background task", e);
            }
        }, 5, 5, TimeUnit.MINUTES);
    }
}
