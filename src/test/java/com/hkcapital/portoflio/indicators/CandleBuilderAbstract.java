package com.hkcapital.portoflio.indicators;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hkcapital.portoflio.service.api.etoro.websocket.LiveInstrumentRate;
import com.hkcapital.portoflio.service.api.etoro.websocket.LivePriceResponseWrapper;
import com.hkcapital.portoflio.service.api.etoro.websocket.Message;
import org.apache.commons.io.IOUtils;

import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

public abstract class CandleBuilderAbstract
{
    public static final String GOLD_ETORO_INSTRUMENT = "18";
    protected static final String expectedText = "{\"messages\":[{";
    protected static final ObjectMapper OBJECT_MAPPER = new ObjectMapper().findAndRegisterModules();

    public List<String> loadTicks(final String fileName) throws IOException
    {
        return IOUtils.readLines(new FileReader(fileName, StandardCharsets.UTF_8));
    }

    public Candle toOneMinuteCandle(final Tick tick)
    {
        return new Candle(tick.getInstrument(), tick.getVal(), tick.getVal(), tick.getVal(), tick.getVal(), tick.getTime(),
                Unit.MINUTE, 1);
    }

    public List<Message> messageFrom(final String line) throws JsonProcessingException
    {
        return OBJECT_MAPPER.readValue(line, LivePriceResponseWrapper.class).getMessages();
    }

    public LiveInstrumentRate rateFromMessage(final Message message) throws JsonProcessingException
    {
        return OBJECT_MAPPER.readValue(message.getContent(), LiveInstrumentRate.class);
    }

    public Tick tickFromRate(final LiveInstrumentRate rate)
    {
        return Tick.builder().instrument(rate.getInstrumentId().toString()) //
                .time(rate.getDate())//
                .val(rate.getAsk()) //
                .build();
    }
}
