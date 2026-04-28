package com.hkcapital.portoflio.service.candle.etoro.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hkcapital.portoflio.broker.etoro.config.EtoroApiConfiguration;
import com.hkcapital.portoflio.indicators.Candle;
import com.hkcapital.portoflio.indicators.CandleBuilder;
import com.hkcapital.portoflio.indicators.Tick;
import com.hkcapital.portoflio.indicators.Unit;
import com.hkcapital.portoflio.model.Instrument;
import com.hkcapital.portoflio.service.api.etoro.impl.StartWebSocketRunner;
import com.hkcapital.portoflio.service.api.etoro.websocket.LiveInstrumentRate;
import com.hkcapital.portoflio.service.api.etoro.websocket.LiveResponseMapper;
import com.hkcapital.portoflio.service.candle.etoro.EtoroCandleService;
import com.hkcapital.portoflio.service.instrument.InstrumentService;
import com.hkcapital.portoflio.service.marketfeed.observer.MarketFeedObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

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
    private final Set<String> subscribedTopics = ConcurrentHashMap.newKeySet();

    private final HttpClient httpClient = HttpClient.newHttpClient();

    private final ScheduledExecutorService scheduler =
            Executors.newSingleThreadScheduledExecutor();

    private volatile WebSocket webSocket;

    CandleBuilder candleBuilder1Min = CandleBuilder
            .build()
            .ofTimeFrame(Unit.MINUTE)
            .ofInterval(1);

    CandleBuilder candleBuilder5Min = CandleBuilder
            .build()
            .ofTimeFrame(Unit.MINUTE)
            .ofInterval(5);
    CandleBuilder candleBuilder15Min = CandleBuilder
            .build()
            .ofTimeFrame(Unit.MINUTE)
            .ofInterval(15);
    CandleBuilder candleBuilder30Min = CandleBuilder
            .build()
            .ofTimeFrame(Unit.MINUTE)
            .ofInterval(30);
    CandleBuilder candleBuilder1Hour = CandleBuilder
            .build()
            .ofTimeFrame(Unit.HOUR)
            .ofInterval(1);
    CandleBuilder candleBuilder4Hour = CandleBuilder
            .build()
            .ofTimeFrame(Unit.HOUR)
            .ofInterval(4);

    private volatile boolean reconnecting = false;

    public EtoroLiveFeedListener(EtoroApiConfiguration apiConfiguration,
                                 MarketFeedObserver marketFeedObserver,
                                 LiveResponseMapper liveResponseMapper,
                                 InstrumentService instrumentService,
                                 ObjectMapper objectMapper,
                                 EtoroCandleService etoroCandleService)
    {
        this.apiConfiguration = apiConfiguration;
        this.marketFeedObserver = marketFeedObserver;
        this.liveResponseMapper = liveResponseMapper;
        this.instrumentService = instrumentService;
        this.objectMapper = objectMapper;
        this.etoroCandleService = etoroCandleService;
    }

    @Override
    public void onOpen(WebSocket webSocket)
    {
        logger.info("WebSocket connected");
        this.webSocket = webSocket;
        reconnecting = false;
        subscribedTopics.clear();
        performAuth(webSocket, apiConfiguration);
        webSocket.request(1);
    }

    @Override
    public CompletionStage<?> onText(WebSocket ws, CharSequence data, boolean last)
    {
        buffer.append(data);
        if(last) {
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
                marketFeedObserver.process(liveInstrumentRate);
                if (liveInstrumentRate != null && liveInstrumentRate.getAsk() != null)
                {
                    Tick tick = tickFromRate(liveInstrumentRate);
                    candleBuilder1Min.setCandleService(etoroCandleService);
                    candleBuilder5Min.setCandleService(etoroCandleService);
                    candleBuilder15Min.setCandleService(etoroCandleService);
                    candleBuilder30Min.setCandleService(etoroCandleService);
                    candleBuilder1Hour.setCandleService(etoroCandleService);
                    candleBuilder4Hour.setCandleService(etoroCandleService);

                    candleBuilder1Min.addAndUpdateCandle(toCandle(tick, Unit.MINUTE, 1));
                    candleBuilder5Min.addAndUpdateCandle(toCandle(tick, Unit.MINUTE, 5));
                    candleBuilder15Min.addAndUpdateCandle(toCandle(tick, Unit.MINUTE, 15));
                    candleBuilder30Min.addAndUpdateCandle(toCandle(tick, Unit.MINUTE, 30));
                    candleBuilder1Hour.addAndUpdateCandle(toCandle(tick, Unit.HOUR, 1));
                    candleBuilder4Hour.addAndUpdateCandle(toCandle(tick, Unit.HOUR, 4));
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
        reconnect(StartWebSocketRunner.ETORO_WEB_SOCKET_URL);
    }



    @Override
    public CompletionStage<?> onClose(WebSocket webSocket, int statusCode, String reason)
    {
        logger.warn("WebSocket closed [{}] {}", statusCode, reason);
        candleBuilder1Min.flush();
        candleBuilder5Min.flush();
        candleBuilder15Min.flush();
        candleBuilder30Min.flush();
        candleBuilder1Hour.flush();
        candleBuilder4Hour.flush();
        reconnect(StartWebSocketRunner.ETORO_WEB_SOCKET_URL);
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
                            reconnect(StartWebSocketRunner.ETORO_WEB_SOCKET_URL);
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

    public Candle toCandle(final Tick tick, Unit unit , Integer interval)
    {
        return new Candle(tick.getInstrument(), tick.getVal(), tick.getVal(), tick.getVal(), tick.getVal(), tick.getTime(),
                unit, interval);
    }

}