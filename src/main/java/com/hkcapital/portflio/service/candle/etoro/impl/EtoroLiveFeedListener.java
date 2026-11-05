package com.hkcapital.portflio.service.candle.etoro.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hkcapital.portflio.broker.etoro.config.EtoroApiConfiguration;
import com.hkcapital.portflio.broker.etoro.config.TradingConfiguration;
import com.hkcapital.portflio.config.SimulationConfig;
import com.hkcapital.portflio.market.indicators.CandleBuilder;
import com.hkcapital.portflio.market.indicators.CandleDto;
import com.hkcapital.portflio.market.indicators.Tick;
import com.hkcapital.portflio.market.indicators.TimeFramesUnit;
import com.hkcapital.portflio.market.structure.MarketStructureCache;
import com.hkcapital.portflio.model.Candle;
import com.hkcapital.portflio.model.Instrument;
import com.hkcapital.portflio.model.InstrumentMarketStructureConf;
import com.hkcapital.portflio.model.TradingIndicator;
import com.hkcapital.portflio.repository.registry.ServiceRegistery;
import com.hkcapital.portflio.service.api.etoro.websocket.LiveInstrumentRate;
import com.hkcapital.portflio.service.api.etoro.websocket.LiveResponseMapper;
import com.hkcapital.portflio.service.bandlogger.Bandlogger;
import com.hkcapital.portflio.service.candle.etoro.EtoroCandleService;
import com.hkcapital.portflio.service.env.EnvService;
import com.hkcapital.portflio.service.instrument.InstrumentService;
import com.hkcapital.portflio.service.instrumentmarketstructureconf.InstrumentMarketStructureConfService;
import com.hkcapital.portflio.service.marketfeed.observer.MarketFeedObserver;
import com.hkcapital.portflio.service.orders.IndicatorProcessor;
import com.hkcapital.portflio.service.registry.Service;
import com.hkcapital.portflio.service.tradingindicators.IndicatorProcessorsFactory;
import com.hkcapital.portflio.service.tradingindicators.TradingIndicatorsService;
import com.hkcapital.portflio.ui.chart.LiveMarketChart;
import com.hkcapital.portflio.util.DateTimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.WebSocket;
import java.net.http.WebSocket.Listener;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

@Component
@Slf4j
public class EtoroLiveFeedListener implements Listener
{
    public static final String SIMULATION = "simulation";
    public static final String SIMULATION_DATA_PATH = "{\"data\":{\"dataFolder\":\"%s\"}}";
    public static final String OPERATION = "operation";
    public static final String AUTHENTICATE = "Authenticate";
    public static final String SUCCESS = "success";
    private StringBuilder buffer = new StringBuilder();
    private final EtoroApiConfiguration apiConfiguration;
    private final MarketFeedObserver marketFeedObserver;
    private final LiveResponseMapper liveResponseMapper;
    private final InstrumentService instrumentService;
    private final ObjectMapper objectMapper;

    private static LiveMarketChart instance;
    private final Set<String> subscribedTopics = ConcurrentHashMap.newKeySet();

    private final HttpClient httpClient = HttpClient.newHttpClient();

    private final EnvService envService;

    private final ServiceRegistery<Service> serRgstry;
    private final ScheduledExecutorService scheduler =
            Executors.newSingleThreadScheduledExecutor();
    private final MarketStructureCache marktStctrMgCache;
    private final SimulationConfig simulationConfig;

    private final EtoroCandleService candleService;

    final List<Instrument> candleInstruments;
    final SignalBuilder signalBuilder = SignalBuilder.builder().build();

    private volatile boolean reconnecting = false;

    public EtoroLiveFeedListener(final EtoroApiConfiguration apiConfiguration,
                                 final MarketFeedObserver marketFeedObserver,
                                 final LiveResponseMapper liveResponseMapper,
                                 final InstrumentService instrumentService,
                                 final ObjectMapper objectMapper,
                                 final EtoroCandleService etoroCandleService,
                                 final MarketStructureCache marketStructureManagerCache,
                                 final Bandlogger bandlogger,
                                 final EnvService envService,
                                 final InstrumentMarketStructureConfService instMarketStrConfSrv,
                                 final SimulationConfig simulationConfig,
                                 final TradingIndicatorsService indicatorsService,
                                 final EtoroCandleService candleService,
                                 final ServiceRegistery<Service> serviceRegistery)
    {
        this.apiConfiguration = apiConfiguration;
        this.marketFeedObserver = marketFeedObserver;
        this.liveResponseMapper = liveResponseMapper;
        this.instrumentService = instrumentService;
        this.objectMapper = objectMapper;
        this.marktStctrMgCache = marketStructureManagerCache;
        this.envService = envService;
        this.simulationConfig = simulationConfig;
        this.serRgstry = serviceRegistery;
        this.candleService = candleService;

        candleInstruments = instrumentService.findByActiveAndWithCandle(Boolean.TRUE, Boolean.TRUE);

        final List<Instrument> instrumentList = instrumentService.findByActive(Boolean.TRUE);

        final List<TradingIndicator> indicators = indicatorsService.findAll();

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
                signalBuilder.getCandleBuilder().add(candleBuilder);
            }
            signalBuilder.getInstruments().add(inst);
        }

        for (CandleBuilder e : signalBuilder.getCandleBuilder())
        {
            final Map<String, IndicatorProcessor> processors = new HashMap<>();

            for(TradingIndicator indicator : indicators)
            {
                if(e.getTimeFrame().getUnit().equals(indicator.getTimeFrameUnit())
                        && e.getInterval().intValue() == indicator.getTimeFrame() &&
                        e.getInstrument().getInstrumentTicker()
                                .equals(e.getInstrument().getInstrumentTicker())
                )
                {
                    processors.put(indicator.getKey(), IndicatorProcessorsFactory.createProcessor(indicator));
                }
            }
            e.addIndicators(processors);
        }

        LocalDateTime dateTime = LocalDateTime.now();

        LocalDateTime startOfDay = dateTime.toLocalDate().atStartOfDay().plusDays(-14);
        LocalDateTime endOfDay = dateTime.toLocalDate().atTime(LocalTime.MAX);

        List<Candle> candleList =
                candleService.findByCreationDateTimeBetweenOrderByCreationDateTimeAsc(startOfDay, endOfDay);

        for (CandleBuilder e : signalBuilder.getCandleBuilder())
        {
            for(Candle candle : candleList)
            {
                if(e.getInstrument().getInstrumentTicker()
                        .equals(candle.getInstrument().getInstrumentTicker())
                && e.getTimeFrame().getUnit().equals(candle.getTimeFrameUnit())
                && e.getInterval().intValue() == candle.getTimeFrame().intValue())
                {
                    TimeFramesUnit timeFramesUnit = TimeFramesUnit.MINUTE;

                    if(candle.getTimeFrameUnit().equals(TimeFramesUnit.HOUR.getUnit()))
                    {
                        timeFramesUnit = TimeFramesUnit.HOUR;
                    }

                    if(candle.getTimeFrameUnit().equals(TimeFramesUnit.SECOND.getUnit()))
                    {
                        timeFramesUnit = TimeFramesUnit.SECOND;
                    }

                    if(candle.getTimeFrameUnit().equals(TimeFramesUnit.DAY.getUnit()))
                    {
                        timeFramesUnit = TimeFramesUnit.DAY;
                    }

                    if(candle.getTimeFrameUnit().equals(TimeFramesUnit.WEEK.getUnit()))
                    {
                        timeFramesUnit = TimeFramesUnit.WEEK;
                    }

                    CandleDto candleDto = CandleDto.builder().close(candle.getClose())
                            .low(candle.getLow())
                            .high(candle.getHigh())
                            .open(candle.getOpen())
                            .instrument(candle.getInstrument())
                            .timeFramesUnit(timeFramesUnit)
                            .interval(candle.getTimeFrame())
                            .time(candle.getFromDate())
                            .lowTime(candle.getLowTime())
                            .highTime(candle.getHighTime())
                            .build();
                    e.getCandles().add(candleDto);
                }
            }
        }

        signalBuilder.getCandleBuilder().forEach(candleBuilder ->
        {
            candleBuilder.initIndicator();
        });

    }

    @Override
    public void onOpen(WebSocket webSocket)
    {
        log.info("WebSocket connected");
        reconnecting = false;
        subscribedTopics.clear();
        if (envService.getActiveProfile().equals(SIMULATION))
        {
            performAuthSimulation(webSocket, apiConfiguration);
        } else
        {
            performAuth(webSocket, apiConfiguration);
        }

        if (envService.getActiveProfile().equals(SIMULATION))
        {
            String dataPath = simulationConfig.getDataFolder();
            String data = String.format(
                    SIMULATION_DATA_PATH,
                    dataPath
            );
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
                if (node.has(OPERATION) &&
                        AUTHENTICATE.equals(node.get(OPERATION).asText()) &&
                        node.path(SUCCESS).asBoolean(false))
                {
                    log.info("Authentication successful");
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
                    signalBuilder.getCandleBuilder().forEach(candleBuilder ->
                    {
                        if (liveInstrumentRate.getInstrumentId() //
                                == candleBuilder.getInstrument().getEtoroInstrumentId().intValue()
                                && candleBuilder.getInstrument().getWithCandle().booleanValue()
                        )
                        {
                            CandleDto candleDto = toCandle(tick, candleBuilder.getTimeFrame(), candleBuilder.getInterval());
                            if (candleDto != null)
                            {
                                candleBuilder.addAndUpdateCandle(candleDto, candleBuilder.getInstrument());
                            }
                        }
                    });
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
                    marketFeedObserver.process(liveInstrumentRate, signalBuilder);
                }


            } catch (JsonProcessingException e)
            {
                log.error("JSON parse error", data);
            }
        }
        ws.request(1);
        return CompletableFuture.completedFuture(null);
    }

    @Override
    public void onError(WebSocket webSocket, Throwable error)
    {
        log.error("WebSocket error", error);
        reconnect(apiConfiguration.getUrl());
    }

    @Override
    public CompletionStage<?> onClose(WebSocket webSocket, int statusCode, String reason)
    {
        log.warn("WebSocket closed [{}] {}", statusCode, reason);
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

        log.info("Authentication sent");
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

        log.info("Authentication sent");
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

        log.info("Subscribed instrument {}", instrumentId);
    }

    private void reconnect(String url)
    {
        if (reconnecting)
        {
            return;
        }

        reconnecting = true;

        log.warn("Reconnecting to eToro WebSocket...");

        scheduler.schedule(() ->
        {
            httpClient.newWebSocketBuilder()
                    .buildAsync(URI.create(url), this)
                    .whenComplete((ws, error) ->
                    {
                        if (error != null)
                        {
                            log.error("Reconnect failed", error);
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
        for (Instrument inst : candleInstruments)
        {
            if (Integer.parseInt(tick.getInstrument()) == inst.getEtoroInstrumentId())
            {
                return new CandleDto(inst, tick.getVal(), tick.getVal(), tick.getTime(),
                        tick.getVal(), tick.getTime(), tick.getVal(), tick.getTime(),
                        timeFramesUnit, interval);
            }
        }
        return null;
    }

}