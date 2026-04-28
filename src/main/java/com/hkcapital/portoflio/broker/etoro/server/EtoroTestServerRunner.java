package com.hkcapital.portoflio.broker.etoro.server;

import lombok.extern.slf4j.Slf4j;
import org.glassfish.tyrus.server.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Slf4j
public class EtoroTestServerRunner
{
    private static final Logger logger = LoggerFactory.getLogger(EtoroTestServerRunner.class);
    public static void main(String[] args) throws Exception {
        Server server = new Server("localhost", 8025, "/ws", null, //
                EtoroTestServer.class);
        server.start();
        logger.info("Etoro Simulated Server started!!");
        Thread.currentThread().join();
    }
}
