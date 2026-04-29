package com.hkcapital.portoflio.service.api.etoro.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hkcapital.portoflio.broker.etoro.config.EtoroApiConfiguration;
import com.hkcapital.portoflio.service.api.etoro.websocket.LiveResponseMapper;
import com.hkcapital.portoflio.service.candle.etoro.EtoroCandleService;
import com.hkcapital.portoflio.service.marketfeed.subscriber.impl.BuySellSignalGeneratorSub;
import com.hkcapital.portoflio.service.marketfeed.subscriber.impl.MarketFeedDbWriterSub;
import com.hkcapital.portoflio.service.instrument.InstrumentService;
import com.hkcapital.portoflio.service.marketfeed.observer.MarketFeedObserver;
import com.hkcapital.portoflio.service.orders.OrderManagerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static java.util.concurrent.Executors.newSingleThreadScheduledExecutor;

@Service
public class EtoroWebServiceSocketManagerImpl implements com.hkcapital.portoflio.service.api.etoro.EtoroWebSocketManagerService
{
    private final Logger logger = LoggerFactory.getLogger(EtoroWebServiceSocketManagerImpl.class);

    private final com.hkcapital.portoflio.service.orders.OrderManagerService orderManagerService;
    private final com.hkcapital.portoflio.service.instrument.InstrumentService instrumentService;

    private final EtoroApiConfiguration etoroApiConfiguration;
    private final ObjectMapper objectMapper;

    private final MarketFeedObserver marketFeedObserver;
    private final LiveResponseMapper liveResponseMapper;
    private final MarketFeedDbWriterSub marketFeedDbWriter;
    private final BuySellSignalGeneratorSub buySellManager;

    private final EtoroCandleService etoroCandleService;

    public EtoroWebServiceSocketManagerImpl(final com.hkcapital.portoflio.service.srmatrix.SRMatrixService srMatrixService, //
                                            final OrderManagerService orderManagerService, //
                                            final InstrumentService instrumentService, //
                                            final com.hkcapital.portoflio.service.strategy.StrategyService strategyService, //
                                            final com.hkcapital.portoflio.service.positions.PositionService positionService, //
                                            final EtoroApiConfiguration etoroApiConfiguration, //
                                            final ObjectMapper objectMapper, //
                                            final MarketFeedObserver marketFeedObserver, //
                                            final LiveResponseMapper liveResponseMapper,
                                            final MarketFeedDbWriterSub marketFeedDbWriter,
                                            final BuySellSignalGeneratorSub buySellManager,
                                            final EtoroCandleService etoroCandleService)
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
    }

    @Override
    public void subscribeAndSchedule()
    {

        StartWebSocketRunner startWebSocket = //
                new StartWebSocketRunner(etoroApiConfiguration, marketFeedObserver, //
                        liveResponseMapper, instrumentService, objectMapper, etoroCandleService);
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
