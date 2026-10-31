package com.hkcapital.portflio.indicators;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hkcapital.portflio.etoro.EtoroAbstractTest;
import com.hkcapital.portflio.etoro.TickHelper;
import com.hkcapital.portflio.market.indicators.CandleDto;
import com.hkcapital.portflio.market.indicators.Tick;
import com.hkcapital.portflio.market.indicators.TimeFramesUnit;
import com.hkcapital.portflio.service.api.etoro.websocket.LiveInstrumentRate;
import com.hkcapital.portflio.service.api.etoro.websocket.LivePriceResponseWrapper;
import com.hkcapital.portflio.service.api.etoro.websocket.Message;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public abstract class CandleBuilderAbstract extends EtoroAbstractTest
{
    public static final String GOLD_ETORO_INSTRUMENT = "18";
    protected static final String expectedText = "{\"messages\":[{";
    protected static final ObjectMapper OBJECT_MAPPER = new ObjectMapper().findAndRegisterModules();

    public CandleDto toOneMinuteCandle(final Tick tick)
    {
        return new CandleDto(null, tick.getVal(), tick.getVal(), tick.getTime(), tick.getVal(), tick.getTime(),
                tick.getVal(), tick.getTime(),
                TimeFramesUnit.MINUTE, 1);
    }

    public List<Message> messageFrom(final String line) throws JsonProcessingException
    {
        return OBJECT_MAPPER.readValue(line, LivePriceResponseWrapper.class).getMessages();
    }

    public LiveInstrumentRate rateFromMessage(final Message message) throws JsonProcessingException
    {
        return OBJECT_MAPPER.readValue(message.getContent(), LiveInstrumentRate.class);
    }

}
