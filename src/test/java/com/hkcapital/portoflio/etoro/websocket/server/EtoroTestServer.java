package com.hkcapital.portoflio.etoro.websocket.server;

import jakarta.websocket.EndpointConfig;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint(value = "/etoro")
public class EtoroTestServer
{
    private boolean authenticated = false;
    private static final String apiKey = "sdgdskldFPLGfjHn1421dgnlxdGTbngdflg6290bRjslfihsjhSDsdgGHH25hjf";
    private static final String userKey = "eyJjaSI6IjYwY2FiYjBiLTU1OTctNDQ4NS04ZjYzLTdlOWUwNTZlMGJiOCIsImVhbiI6IlVucmVnaXN0ZXJlZEFwcGxpY2F0aW9uIiwiZWsiOiJQSGVOS0kzeklPUDhpenNyeW1qNnhrV1JoVFNuVkJld0Z0RkdvSTJWMmRFR2pKNXAuYWV5WW9nUDNvTzVqQ0hkTFVZSUx2N0pRLmdIUTh0cTZXeldLYkJGaGNSclIyTGl0emF4UnJ3WDhNQV8ifQ__";
    private static final Map<Session, Set<String>> subscriptions = new ConcurrentHashMap<>();
    private final static String GOLD_NASDAQ100_LIVE_FEED_FILE_PATH = //
            "D:/portfolio-pnl-simulator/src/test/data/livefeed-etoro/nasda-gold-btc-live-feed-17.03.2026.log";

    @OnMessage
    public void onMessage(Session session, String message)
    {
        try
        {
            JSONObject apiKeyMessage = new JSONObject(message);
            JSONObject apiKeys = apiKeyMessage.getJSONObject("data");
            if (apiKey.equals(apiKeys.optString("apiKey")) &&
                    userKey.equals(apiKeys.optString("userKey")))
            {
                authenticated = true;
                System.out.println("Successful authentication!");
                return;
            }
            if (!authenticated)
            {
                System.out.println("Unsuccessful authentication!");
                session.close();
                return;
            }
            JSONObject json = new JSONObject(message);
            if ("Subscribe".equals(json.getString("operation")))
            {
                JSONObject data = json.getJSONObject("data");
                JSONArray topics = data.getJSONArray("topics");
                boolean snapshot = data.getBoolean("snapshot");
                Set<String> clientTopics = subscriptions
                        .computeIfAbsent(session, s -> new HashSet<>());
                for (int i = 0; i < topics.length(); i++)
                {
                    clientTopics.add(topics.getString(i));
                }
                System.out.println("Client subscribed to: " + clientTopics);
                if (snapshot)
                {
                    for (String topic : clientTopics)
                    {
                        String line;
                        List<String> messages = new ArrayList<>();
                        try (BufferedReader br = new BufferedReader(new FileReader(GOLD_NASDAQ100_LIVE_FEED_FILE_PATH)))
                        {
                            while ((line = br.readLine()) != null)
                            {
                                messages.add(line);
                            }
                        }
                        new Thread(() ->
                        {
                            try
                            {
                                for (String m : messages)
                                {
                                    session.getBasicRemote().sendText(m);
                                    Thread.sleep(500);
                                }
                            } catch (Exception e)
                            {
                                e.printStackTrace();
                            }
                        }).start();
                    }
                }
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @OnOpen
    public void onOpen(Session session, EndpointConfig config) throws IOException
    {
        System.out.println("Connecting to fake etoro server!!!");
    }

}
