package com.hkcapital.portoflio.etoro.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@Import(ObjectMapper.class)
class LivePriceResponseWrapperTest extends LiveFeedAbstractTest
{
    @Test
    void testLogFileContainsExpectedText()
    {
        LivePriceResponseWrapper livePriceResponseWrapper;
        String filePath = "D:/portfolio-pnl-simulator/src/test/data/livefeed-etoro/nasda-gold-btc-live-feed-16.03.2026.log"; // <-- replace with your log file path
        String expectedText = "{\"messages\":[{";
        boolean found = false;
        int count = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(filePath)))
        {
            String line;
            while ((line = br.readLine()) != null)
            {
                if (line.contains(expectedText))
                {
                    livePriceResponseWrapper = objectMapper.readValue(line, LivePriceResponseWrapper.class);
                    for (Message message : livePriceResponseWrapper.getMessages())
                    {
                            System.out.println("Line no = ["+count+"] "+message.getContent());
                            count++;
                    }
                }
            }
        } catch (IOException e)
        {
            e.printStackTrace();
        }


    }
}
