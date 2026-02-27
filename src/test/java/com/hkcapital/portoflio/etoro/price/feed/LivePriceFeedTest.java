package com.hkcapital.portoflio.etoro.price.feed;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hkcapital.portoflio.etoro.websocket.LiveInstrumentRate;
import com.hkcapital.portoflio.etoro.websocket.LivePriceResponseWrapper;
import com.hkcapital.portoflio.etoro.websocket.Message;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class LivePriceFeedTest
{
    private final ObjectMapper mapper = new ObjectMapper();
    @Test
    public void parsePriceFeedTest() throws IOException
    {
        InputStream inputStream= this.getClass().getClassLoader().getResourceAsStream("price-live-feed.json");
        LivePriceResponseWrapper messageList = mapper.readValue(inputStream.readAllBytes(), LivePriceResponseWrapper.class);

        LiveInstrumentRate instrumentRate = mapper.readValue(messageList.getMessages().get(0).getContent(), //
                LiveInstrumentRate.class);

        System.out.println(instrumentRate);
    }
}
