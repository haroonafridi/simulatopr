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
import com.hkcapital.portflio.repository.registry.ServiceRegistery;
import com.hkcapital.portflio.service.api.etoro.websocket.LiveInstrumentRate;
import com.hkcapital.portflio.service.api.etoro.websocket.LiveResponseMapper;
import com.hkcapital.portflio.service.bandlogger.Bandlogger;
import com.hkcapital.portflio.service.candle.etoro.EtoroCandleService;
import com.hkcapital.portflio.service.env.EnvService;
import com.hkcapital.portflio.service.instrument.InstrumentService;
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

    private final ServiceRegistery<Service> serviceRegistery;
    private final ScheduledExecutorService scheduler =
            Executors.newSingleThreadScheduledExecutor();
    private final MarketStructureCache marketStructureManagerCache;

    private volatile WebSocket webSocket;

    CandleBuilder candleBuilder1MinGold = CandleBuilder
            .build()
            .ofTimeFrame(TimeFramesUnit.MINUTE)
            .ofInterval(1);
    CandleBuilder candleBuilder5MinGold = CandleBuilder
            .build()
            .ofTimeFrame(TimeFramesUnit.MINUTE)
            .ofInterval(5);
    CandleBuilder candleBuilder15MinGold = CandleBuilder
            .build()
            .ofTimeFrame(TimeFramesUnit.MINUTE)
            .ofInterval(15);
    CandleBuilder candleBuilder30MinGold = CandleBuilder
            .build()
            .ofTimeFrame(TimeFramesUnit.MINUTE)
            .ofInterval(30);
    CandleBuilder candleBuilder1HourGold = CandleBuilder
            .build()
            .ofTimeFrame(TimeFramesUnit.HOUR)
            .ofInterval(1);
    CandleBuilder candleBuilder4HourGold = CandleBuilder
            .build()
            .ofTimeFrame(TimeFramesUnit.HOUR)
            .ofInterval(4);


    CandleBuilder candleBuilder1MinNasdaq = CandleBuilder
            .build()
            .ofTimeFrame(TimeFramesUnit.MINUTE)
            .ofInterval(1);
    CandleBuilder candleBuilder5MinNasdaq = CandleBuilder
            .build()
            .ofTimeFrame(TimeFramesUnit.MINUTE)
            .ofInterval(5);
    CandleBuilder candleBuilder15MinNasdaq = CandleBuilder
            .build()
            .ofTimeFrame(TimeFramesUnit.MINUTE)
            .ofInterval(15);
    CandleBuilder candleBuilder30MinNasdaq = CandleBuilder
            .build()
            .ofTimeFrame(TimeFramesUnit.MINUTE)
            .ofInterval(30);
    CandleBuilder candleBuilder1HourNasdaq = CandleBuilder
            .build()
            .ofTimeFrame(TimeFramesUnit.HOUR)
            .ofInterval(1);
    CandleBuilder candleBuilder4HourNasdaq = CandleBuilder
            .build()
            .ofTimeFrame(TimeFramesUnit.HOUR)
            .ofInterval(4);




    SignalBuilder signalBuilderGold = SignalBuilder.builder()
            .candleBuilder1Min(candleBuilder1MinGold)
            .candleBuilder5Min(candleBuilder5MinGold)
            .candleBuilder15Min(candleBuilder15MinGold)
            .candleBuilder30Min(candleBuilder30MinGold)
            .candleBuilder1Hour(candleBuilder1HourGold)
            .candleBuilder4Hour(candleBuilder4HourGold)
            .build();


    SignalBuilder signalBuilderNasdaq = SignalBuilder.builder()
            .candleBuilder1Min(candleBuilder1MinNasdaq)
            .candleBuilder5Min(candleBuilder5MinNasdaq)
            .candleBuilder15Min(candleBuilder15MinNasdaq)
            .candleBuilder30Min(candleBuilder30MinNasdaq)
            .candleBuilder1Hour(candleBuilder1HourNasdaq)
            .candleBuilder4Hour(candleBuilder4HourNasdaq)
            .build();


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
                                 ServiceRegistery<Service> serviceRegistery)
    {
        this.apiConfiguration = apiConfiguration;
        this.marketFeedObserver = marketFeedObserver;
        this.liveResponseMapper = liveResponseMapper;
        this.instrumentService = instrumentService;
        this.objectMapper = objectMapper;
        this.etoroCandleService = etoroCandleService;
        this.marketStructureManagerCache = marketStructureManagerCache;
        this.bandlogger = bandlogger;
        this.envService = envService;
        this.serviceRegistery = serviceRegistery;

        List<Instrument> instrumentList = instrumentService.findByActive(Boolean.TRUE);

        if(instrumentList.size() == 0)
        {
            return;
        }
        Instrument gold = instrumentList
                .stream()
                .filter(g->g.getEtoroInstrumentId() == 18)
                .collect(Collectors.toList()).get(0);

        Instrument nasdaq = instrumentList
                .stream()
                .filter(g->g.getEtoroInstrumentId() == 28)
                .collect(Collectors.toList()).get(0);


        candleBuilder1MinGold.setInstrument(gold);
        candleBuilder5MinGold.setInstrument(gold);
        candleBuilder15MinGold.setInstrument(gold);
        candleBuilder30MinGold.setInstrument(gold);
        candleBuilder1HourGold.setInstrument(gold);
        candleBuilder4HourGold.setInstrument(gold);

        candleBuilder1MinNasdaq.setInstrument(nasdaq);
        candleBuilder5MinNasdaq.setInstrument(nasdaq);
        candleBuilder15MinNasdaq.setInstrument(nasdaq);
        candleBuilder30MinNasdaq.setInstrument(nasdaq);
        candleBuilder1HourNasdaq.setInstrument(nasdaq);
        candleBuilder4HourNasdaq.setInstrument(nasdaq);

        candleBuilder1MinGold.marketStructureManagerCache(marketStructureManagerCache);
        candleBuilder1MinGold.bandLogger(bandlogger);
        candleBuilder1MinGold.objectMapper(objectMapper);
        candleBuilder5MinGold.marketStructureManagerCache(marketStructureManagerCache);
        candleBuilder5MinGold.bandLogger(bandlogger);
        candleBuilder5MinGold.objectMapper(objectMapper);
        candleBuilder15MinGold.marketStructureManagerCache(marketStructureManagerCache);
        candleBuilder15MinGold.bandLogger(bandlogger);
        candleBuilder15MinGold.objectMapper(objectMapper);
        candleBuilder30MinGold.marketStructureManagerCache(marketStructureManagerCache);
        candleBuilder30MinGold.bandLogger(bandlogger);
        candleBuilder30MinGold.objectMapper(objectMapper);
        candleBuilder1HourGold.marketStructureManagerCache(marketStructureManagerCache);
        candleBuilder1HourGold.bandLogger(bandlogger);
        candleBuilder1HourGold.objectMapper(objectMapper);
        candleBuilder4HourGold.marketStructureManagerCache(marketStructureManagerCache);
        candleBuilder4HourGold.bandLogger(bandlogger);
        candleBuilder4HourGold.objectMapper(objectMapper);


        candleBuilder1MinNasdaq.marketStructureManagerCache(marketStructureManagerCache);
        candleBuilder1MinNasdaq.bandLogger(bandlogger);
        candleBuilder1MinNasdaq.objectMapper(objectMapper);
        candleBuilder5MinNasdaq.marketStructureManagerCache(marketStructureManagerCache);
        candleBuilder5MinNasdaq.bandLogger(bandlogger);
        candleBuilder5MinNasdaq.objectMapper(objectMapper);
        candleBuilder15MinNasdaq.marketStructureManagerCache(marketStructureManagerCache);
        candleBuilder15MinNasdaq.bandLogger(bandlogger);
        candleBuilder15MinNasdaq.objectMapper(objectMapper);
        candleBuilder30MinNasdaq.marketStructureManagerCache(marketStructureManagerCache);
        candleBuilder30MinNasdaq.bandLogger(bandlogger);
        candleBuilder30MinNasdaq.objectMapper(objectMapper);
        candleBuilder1HourNasdaq.marketStructureManagerCache(marketStructureManagerCache);
        candleBuilder1HourNasdaq.bandLogger(bandlogger);
        candleBuilder1HourNasdaq.objectMapper(objectMapper);
        candleBuilder4HourNasdaq.marketStructureManagerCache(marketStructureManagerCache);
        candleBuilder4HourNasdaq.bandLogger(bandlogger);
        candleBuilder4HourNasdaq.objectMapper(objectMapper);

    }

    @Override
    public void onOpen(WebSocket webSocket)
    {
        logger.info("WebSocket connected");
        this.webSocket = webSocket;
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
                logger.info("{}", data);
                LiveInstrumentRate liveInstrumentRate =
                        liveResponseMapper.mapResponse(data.toString());

                if (liveInstrumentRate != null && liveInstrumentRate.getAsk() != null
                        && liveInstrumentRate.getInstrumentId() == 18)
                {
                    Tick tick = tickFromRate(liveInstrumentRate);
                    candleBuilder1MinGold.setCandleService(etoroCandleService);
                    candleBuilder5MinGold.setCandleService(etoroCandleService);
                    candleBuilder15MinGold.setCandleService(etoroCandleService);
                    candleBuilder30MinGold.setCandleService(etoroCandleService);
                    candleBuilder1HourGold.setCandleService(etoroCandleService);
                    candleBuilder4HourGold.setCandleService(etoroCandleService);
                    SwingUtilities.invokeLater(() ->
                    {
                        candleBuilder1MinGold.addAndUpdateCandle(toCandle(tick, TimeFramesUnit.MINUTE, 1));
                        candleBuilder5MinGold.addAndUpdateCandle(toCandle(tick, TimeFramesUnit.MINUTE, 5));
                        candleBuilder15MinGold.addAndUpdateCandle(toCandle(tick, TimeFramesUnit.MINUTE, 15));
                        candleBuilder30MinGold.addAndUpdateCandle(toCandle(tick, TimeFramesUnit.MINUTE, 30));
                        candleBuilder1HourGold.addAndUpdateCandle(toCandle(tick, TimeFramesUnit.HOUR, 1));
                        candleBuilder4HourGold.addAndUpdateCandle(toCandle(tick, TimeFramesUnit.HOUR, 4));
                    });

                    marketFeedObserver.process(liveInstrumentRate, signalBuilderGold);
                }


                if (liveInstrumentRate != null && liveInstrumentRate.getAsk() != null
                        && liveInstrumentRate.getInstrumentId() == 28)
                {
                    Tick tick = tickFromRate(liveInstrumentRate);
                    candleBuilder1MinNasdaq.setCandleService(etoroCandleService);
                    candleBuilder5MinNasdaq.setCandleService(etoroCandleService);
                    candleBuilder15MinNasdaq.setCandleService(etoroCandleService);
                    candleBuilder30MinNasdaq.setCandleService(etoroCandleService);
                    candleBuilder1HourNasdaq.setCandleService(etoroCandleService);
                    candleBuilder4HourNasdaq.setCandleService(etoroCandleService);
                    SwingUtilities.invokeLater(() ->
                    {
                        candleBuilder1MinNasdaq.addAndUpdateCandle(toCandle(tick, TimeFramesUnit.MINUTE, 1));
                        candleBuilder5MinNasdaq.addAndUpdateCandle(toCandle(tick, TimeFramesUnit.MINUTE, 5));
                        candleBuilder15MinNasdaq.addAndUpdateCandle(toCandle(tick, TimeFramesUnit.MINUTE, 15));
                        candleBuilder30MinNasdaq.addAndUpdateCandle(toCandle(tick, TimeFramesUnit.MINUTE, 30));
                        candleBuilder1HourNasdaq.addAndUpdateCandle(toCandle(tick, TimeFramesUnit.HOUR, 1));
                        candleBuilder4HourNasdaq.addAndUpdateCandle(toCandle(tick, TimeFramesUnit.HOUR, 4));
                    });

                    marketFeedObserver.process(liveInstrumentRate, signalBuilderGold);
                }

                if (TradingConfiguration.SHOW_TRADING)
                {
                    if (instance == null)
                    {
                        instance = new LiveMarketChart(marketStructureManagerCache, signalBuilderGold, serviceRegistery);
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
        candleBuilder1MinGold.flush();
        candleBuilder5MinGold.flush();
        candleBuilder15MinGold.flush();
        candleBuilder30MinGold.flush();
        candleBuilder1HourGold.flush();
        candleBuilder4HourGold.flush();
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
                    "topics": ["instrument:%s"],
                    "snapshot": true
                  }
                }
                """.formatted(UUID.randomUUID(), instrumentId);

        webSocket.sendText(subscribeMessage, true);

        subscribedTopics.add(instrumentId);

        logger.info("Subscribed instrument {}", instrumentId);
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