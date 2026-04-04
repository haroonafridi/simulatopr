package com.hkcapital.portoflio.service.impl.etoro.impl;

import com.hkcapital.portoflio.config.EtoroApiConfiguration;
import com.hkcapital.portoflio.etoro.websocket.EToroWSClient;
import com.hkcapital.portoflio.etoro.websocket.EtoroLiveFeedListener;

public class StartWebSocketRunner implements Runnable
{
    private static final String ETORO_WEB_SOCKET_URL = "wss://ws.etoro.com/ws";
    private EToroWSClient eToroWSClient;

    private final EtoroApiConfiguration etoroApiConfiguration;

    StartWebSocketRunner(EToroWSClient eToroWSClient,
                   EtoroApiConfiguration etoroApiConfiguration)
    {
        this.eToroWSClient = eToroWSClient;
        this.etoroApiConfiguration = etoroApiConfiguration;
    }

    @Override
    public void run()
    {
        try
        {
            eToroWSClient.connect(this.etoroApiConfiguration , ETORO_WEB_SOCKET_URL);
        } catch (InterruptedException e)
        {
            throw new RuntimeException(e);
        }
    }
    public EToroWSClient geteToroWSClient()
    {
        return eToroWSClient;
    }
}
