package com.hkcapital.portoflio.broker.etoro.server;


import com.hkcapital.portoflio.indicators.ChronoFieldUtil;
import com.hkcapital.portoflio.service.api.etoro.websocket.LiveInstrumentRate;
import jakarta.websocket.OnMessage;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@ServerEndpoint(value = "/etoro")
@Slf4j
public class EtoroTestServer
{
    private static final Logger logger = LoggerFactory.getLogger(EtoroTestServer.class);
    private boolean authenticated = true;
    private static final String apiKey = "dummyApiKey";
    private static final String userKey = "dummyUserKey";
    private static final Map<Session, Set<String>> subscriptions = new ConcurrentHashMap<>();
    private final static String GOLD_NASDAQ100_LIVE_FEED_FILE_PATH = //
            "D:/gold_data/gold_ticks_20_04_2026.csv";

    @OnMessage
    public void onMessage(Session session, String message)
    {
        if (message.equals("ping"))
        {
            logger.info("Ping Received");
            return;
        }
        try
        {
            JSONObject apiKeyMessage = new JSONObject(message);
            JSONObject apiKeys = apiKeyMessage.getJSONObject("data");
            if (apiKey.equals(apiKeys.optString("apiKey")) &&
                    userKey.equals(apiKeys.optString("userKey")))
            {
                authenticated = true;
                logger.info("Authentication successful");
                return;
            }
            if (!authenticated)
            {
                logger.info("Unsuccessful authentication");
                session.close();
                return;
            }
            List<String> messages = Files.lines(Path.of(GOLD_NASDAQ100_LIVE_FEED_FILE_PATH))
                    .map(line -> buildRate(line))
                    .map(rate -> EtoroMessageBuilder.buildMessage(rate))
                    .collect(Collectors.toList());
            for (String line : messages)
            {
                try
                {
                    session.getAsyncRemote().sendText(line);
                    Thread.sleep(100l);
                } catch (InterruptedException e)
                {
                    throw new RuntimeException(e);
                }
            }

        } catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

    LiveInstrumentRate buildRate(String line)
    {

        String[] fields = line.split(",");
        LiveInstrumentRate rate = new LiveInstrumentRate();
        System.out.println(fields[13]);
        rate.setInstrumentId(18);
        rate.setAllowBuy(true);
        rate.setAllowSell(false);
        rate.setAsk(Double.parseDouble(fields[3]));
        rate.setAskDiscounted(Double.parseDouble(fields[3]));
        rate.setBid(Double.parseDouble(fields[6]));
        rate.setAvailabilityReason(2);
        rate.setLastExecution(Double.parseDouble(fields[3]));
        rate.setConversionRateAsk(1d);
        rate.setConversionRateBid(1d);
        if (!"NULL".equals(fields[13]))
        {
            rate.setDate(ChronoFieldUtil.parse(fields[13].replace("\"", "")));
        }
        rate.setNewUnitMargin(Double.parseDouble(fields[3]));
        rate.setUnitMarginAsk(Double.parseDouble(fields[3]));
        rate.setUnitMarginBid(Double.parseDouble(fields[6]));
        rate.setPriceRateId(Long.parseLong(fields[23]));
        rate.setBidDiscounted(Double.parseDouble(fields[6]));
        rate.setAskDiscounted(Double.parseDouble(fields[3]));
        rate.setUnitMarginBidDiscounted(Double.parseDouble(fields[6]));
        rate.setUnitMarginAskDiscounted(Double.parseDouble(fields[3]));
        rate.setIsInstrumentActive(true);
        rate.setOfficialClosingPrice(Double.parseDouble(fields[3]));
        rate.setIsMarketOpen(true);
        rate.setAllowBuy(true);
        rate.setMaxPositionUnits(32);
        rate.setIsExchangeOpen(true);
        rate.setDelayedAsk(0);
        rate.setDelayedBid(0);
        rate.setAvailabilityReason(2);
        rate.setIsOfficialClosingPrice(true);
        return rate;
    }

}
