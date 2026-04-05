package com.hkcapital.portoflio.service.api.etoro.websocket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class LiveResponseMapper
{
    private final Logger logger = LoggerFactory.getLogger(LiveResponseMapper.class.getName());
    private final ObjectMapper objectMapper;

    public LiveResponseMapper(ObjectMapper objectMapper)
    {
        this.objectMapper = objectMapper;
    }

    public LiveInstrumentRate mapResponse(String message)
    {
        try
        {
            LivePriceResponseWrapper livePriceResponseWrapper = objectMapper.readValue(message, LivePriceResponseWrapper.class);
            LiveInstrumentRate liveInstrumentRate = null;
            if (livePriceResponseWrapper.getMessages() != null && livePriceResponseWrapper.getMessages().size() > 0)
            {
                Message msg = livePriceResponseWrapper.getMessages().get(0);
                liveInstrumentRate = objectMapper.readValue(msg.getContent(), //
                        LiveInstrumentRate.class);
                if (liveInstrumentRate.getInstrumentId() == null)
                {
                    if (msg.getTopic() != null) //
                    {
                        liveInstrumentRate.setInstrumentId(Integer.parseInt(msg.getTopic().split(":")[1]));
                    }

                }
            }
            return liveInstrumentRate;
        } catch (JsonProcessingException e)
        {
            logger.error(" Cannot process message because of error [{}]", e.getMessage());
        }
        return null;
    }
}
