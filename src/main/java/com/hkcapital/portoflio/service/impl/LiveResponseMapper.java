package com.hkcapital.portoflio.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hkcapital.portoflio.etoro.websocket.LiveInstrumentRate;
import com.hkcapital.portoflio.etoro.websocket.LivePriceResponseWrapper;
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
            if (livePriceResponseWrapper.getMessages() != null && livePriceResponseWrapper.getMessages().size() > 0)
            {
                return objectMapper.readValue(livePriceResponseWrapper.getMessages().get(0).getContent(), //
                        LiveInstrumentRate.class);
            }
        } catch (JsonProcessingException e)
        {
            logger.error(" Cannot process message because of error [{}]", e.getMessage());
        }
        return null;
    }
}
