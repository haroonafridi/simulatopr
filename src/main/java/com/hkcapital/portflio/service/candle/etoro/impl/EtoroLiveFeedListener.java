package com.hkcapital.portflio.service.candle.etoro.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hkcapital.portflio.broker.etoro.config.EtoroApiConfiguration;
import com.hkcapital.portflio.broker.etoro.config.TradingConfiguration;
import com.hkcapital.portflio.market.indicators.CandleBuilder;
import com.hkcapital.portflio.market.indicators.CandleDto;
import com.hkcapital.portflio.market.indicators.Tick;
import com.hkcapital.portflio.market.indicators.TimeFramesUnit;
import com.hkcapital.portflio.market.structure.MarketStructureCache;
import com.hkcapital.portflio.model.Instrument;
import com.hkcapital.portflio.model.InstrumentMarketStructureConf;
import com.hkcapital.portflio.repository.registry.ServiceRegistery;
import com.hkcapital.portflio.service.api.etoro.websocket.LiveInstrumentRate;
import com.hkcapital.portflio.service.api.etoro.websocket.LiveResponseMapper;
import com.hkcapital.portflio.service.bandlogger.Bandlogger;
import com.hkcapital.portflio.service.candle.etoro.EtoroCandleService;
import com.hkcapital.portflio.service.env.EnvService;
import com.hkcapital.portflio.service.instrument.InstrumentService;
import com.hkcapital.portflio.service.instrumentmarketstructureconf.InstrumentMarketStructureConfService;
import com.hkcapital.portflio.service.marketfeed.observer.MarketFeedObserver;
import com.hkcapital.portflio.service.registry.Service;
import com.hkcapital.portflio.ui.chart.LiveMarketChart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.WebSocket;
import java.net.http.WebSocket.Listener;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.*;
import java.util.stream.Collectors;

@Component
public class EtoroLiveFeedListener implements Listener
{

    private StringBuilder buffer = new StringBuilder();
    private final Logger logger = LoggerFactory.getLogger(EtoroLiveFeedListener.class);
    private final EtoroApiConfiguration apiConfiguration;
    private final MarketFeedObserver marketFeedObserver;
    private final LiveResponseMapper liveResponseMapper;
    private final InstrumentService instrumentService;
    private final ObjectMapper objectMapper;
    private final EtoroCandleService etoroCandleService;

    private static LiveMarketChart instance;
    private final Bandlogger bandlogger;
    private final Set<String> subscribedTopics = ConcurrentHashMap.newKeySet();

    private final HttpClient httpClient = HttpClient.newHttpClient();

    private final EnvService envService;

    private final ServiceRegistery<Service> serRgstry;
    private final ScheduledExecutorService scheduler =
            Executors.newSingleThreadScheduledExecutor();
    private final MarketStructureCache marktStctrMgCache;
    private final InstrumentMarketStructureConfService instMarketStrConfSrv;

    SignalBuilder signalBuilder = SignalBuilder.builder().build();

    private volatile boolean reconnecting = false;

    public EtoroLiveFeedListener(EtoroApiConfiguration apiConfiguration,
                                 MarketFeedObserver marketFeedObserver,
                                 LiveResponseMapper liveResponseMapper,
                                 InstrumentService instrumentService,
                                 ObjectMapper objectMapper,
                                 EtoroCandleService etoroCandleService,
                                 MarketStructureCache marketStructureManagerCache,
                                 Bandlogger bandlogger,
                                 EnvService envService,
                                 InstrumentMarketStructureConfService instMarketStrConfSrv,
                                 ServiceRegistery<Service> serviceRegistery)
    {
        this.apiConfiguration = apiConfiguration;
        this.marketFeedObserver = marketFeedObserver;
        this.liveResponseMapper = liveResponseMapper;
        this.instrumentService = instrumentService;
        this.objectMapper = objectMapper;
        this.etoroCandleService = etoroCandleService;
        this.marktStctrMgCache = marketStructureManagerCache;
        this.bandlogger = bandlogger;
        this.envService = envService;
        this.serRgstry = serviceRegistery;
        this.instMarketStrConfSrv = instMarketStrConfSrv;

        List<Instrument> instrumentList = instrumentService.findByActive(Boolean.TRUE);

        if (instrumentList.size() == 0)
        {
            return;
        }

        for (Instrument inst : instrumentList)
        {
            List<InstrumentMarketStructureConf> mrktStrConfs = //
                    instMarketStrConfSrv.findByInstrumentAndActiveOrdeyByMarketOrder(inst, Boolean.TRUE);

            for (InstrumentMarketStructureConf mrktStrConf : mrktStrConfs)
            {
                CandleBuilder candleBuilder = CandleBuilder
                        .build()
                        .ofInterval(mrktStrConf.getTimeFrame())
                        .ofTimeFrame(TimeFramesUnit.valueOf(mrktStrConf.getTimeFrameUnit()));
                candleBuilder.setInstrument(inst);
                candleBuilder.marketStructureManagerCache(marketStructureManagerCache);
                candleBuilder.objectMapper(objectMapper);
                candleBuilder.bandLogger(bandlogger);
                candleBuilder.setCandleService(etoroCandleService);
                signalBuilder.getCandleBuilder()
                        .add(candleBuilder);
            }
            signalBuilder.getInstruments().add(inst);
        }
    }

    @Override
    public void onOpen(WebSocket webSocket)
    {
        logger.info("WebSocket connected");
        reconnecting = false;
        subscribedTopics.clear();
        if (envService.getActiveProfile().equals("simulation"))
        {
            performAuthSimulation(webSocket, apiConfiguration);
        } else
        {
            performAuth(webSocket, apiConfiguration);
        }

        if (envService.getActiveProfile().equals("simulation"))
        {
            String data = "{ data:  { value : 2026-08-14} }";
            webSocket.sendText(data, true).join();
        }
        webSocket.request(1);
    }

    @Override
    public CompletionStage<?> onText(WebSocket ws, CharSequence data, boolean last)
    {
        buffer.append(data);
        if (last)
        {
            try
            {
                JsonNode node = objectMapper.readTree(data.toString());
                if (node.has("operation") &&
                        "Authenticate".equals(node.get("operation").asText()) &&
                        node.path("success").asBoolean(false))
                {
                    logger.info("Authentication successful");
                    List<Instrument> instrumentList = instrumentService.findAll()
                            .stream()
                            .filter(instrument -> instrument != null && instrument.getActive())
                            .collect(Collectors.toList());

                    instrumentList.forEach(instrument ->
                    {
                        if (instrument.getEtoroInstrumentId() != null)
                        {
                            subscribeInstrument(ws,
                                    String.valueOf(instrument.getEtoroInstrumentId()));
                        }
                    });
                }
                LiveInstrumentRate liveInstrumentRate =
                        liveResponseMapper.mapResponse(data.toString());

                if (liveInstrumentRate != null && liveInstrumentRate.getAsk() != null)
                {
                    Tick tick = tickFromRate(liveInstrumentRate);
                    logger.info("tick => {}", tick);
                    SwingUtilities.invokeLater(() ->
                    {
                        signalBuilder.getCandleBuilder().forEach(candleBuilder ->
                        {
                            if (liveInstrumentRate.getInstrumentId() == candleBuilder.getInstrument().getEtoroInstrumentId().intValue()
                                    && candleBuilder.getInstrument().getWithCandle().booleanValue()
                            )
                            {
                                candleBuilder.addAndUpdateCandle(toCandle(tick, candleBuilder.getTimeFrame(), candleBuilder.getInterval()));
                            }
                        });
                    });

                    marketFeedObserver.process(liveInstrumentRate, signalBuilder);
                }

                if (TradingConfiguration.SHOW_TRADING)
                {
                    if (instance == null)
                    {
                        instance = new LiveMarketChart(marktStctrMgCache, signalBuilder, serRgstry);
                        instance.display();

                    } else
                    {
                        if (!instance.isVisible())
                        {
                            instance.setVisible(Boolean.TRUE);
                        }
                        instance.handleMarketTick();
                    }
                }
            } catch (JsonProcessingException e)
            {
                logger.error("JSON parse error", data);
            }
        }
        ws.request(1);
        return CompletableFuture.completedFuture(null);
    }

    @Override
    public void onError(WebSocket webSocket, Throwable error)
    {
        logger.error("WebSocket error", error);
        reconnect(apiConfiguration.getUrl());
    }

    @Override
    public CompletionStage<?> onClose(WebSocket webSocket, int statusCode, String reason)
    {
        logger.warn("WebSocket closed [{}] {}", statusCode, reason);
        signalBuilder.getCandleBuilder()
                .forEach(candleBuilder -> candleBuilder.flush());
        reconnect(apiConfiguration.getUrl());
        return CompletableFuture.completedFuture(null);
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

    private void performAuthSimulation(WebSocket ws, EtoroApiConfiguration apiInformation)
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


    public void subscribeInstrument(WebSocket webSocket, String instrumentId)
    {
        if (subscribedTopics.contains(instrumentId))
        {
            return;
        }

        String subscribeMessage = """
                {
                  "id": "%s",
                  "operation": "Subscribe",
                  "data": {
                    "topics": ["instrumentTicker:%s"],
                    "snapshot": true
                  }
                }
                """.formatted(UUID.randomUUID(), instrumentId);

        webSocket.sendText(subscribeMessage, true);

        subscribedTopics.add(instrumentId);

        logger.info("Subscribed instrumentTicker {}", instrumentId);
    }

    private void reconnect(String url)
    {
        if (reconnecting)
        {
            return;
        }

        reconnecting = true;

        logger.warn("Reconnecting to eToro WebSocket...");

        scheduler.schedule(() ->
        {
            httpClient.newWebSocketBuilder()
                    .buildAsync(URI.create(url), this)
                    .whenComplete((ws, error) ->
                    {
                        if (error != null)
                        {
                            logger.error("Reconnect failed", error);
                            reconnecting = false;
                            reconnect(apiConfiguration.getUrl());
                        }
                    });

        }, 3, TimeUnit.SECONDS);
    }

    public Tick tickFromRate(final LiveInstrumentRate rate)
    {
        return Tick.builder().instrument(rate.getInstrumentId().toString()) //
                .time(rate.getDate())//
                .val(rate.getAsk()) //
                .build();
    }

    public CandleDto toCandle(final Tick tick, TimeFramesUnit timeFramesUnit, Integer interval)
    {
        return new CandleDto(tick.getInstrument(), tick.getVal(), tick.getVal(), tick.getTime(),
                tick.getVal(), tick.getTime(), tick.getVal(), tick.getTime(),
                timeFramesUnit, interval);
    }

}