package com.hkcapital.portoflio.indicators;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hkcapital.portoflio.etoro.EtoroAbstractTest;
import com.hkcapital.portoflio.service.api.etoro.websocket.LiveInstrumentRate;
import com.hkcapital.portoflio.service.api.etoro.websocket.LivePriceResponseWrapper;
import com.hkcapital.portoflio.service.api.etoro.websocket.Message;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;

public abstract class CandleBuilderAbstract
{
    public static final String GOLD_ETORO_INSTRUMENT = "18";
    protected static final String expectedText = "{\"messages\":[{";
    protected static final ObjectMapper OBJECT_MAPPER = new ObjectMapper().findAndRegisterModules();

    public List<String> loadData(final String fileName) throws IOException
    {
        return IOUtils.readLines(loadResource(fileName));
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

    public LiveInstrumentRate rateFromString(final String line) throws JsonProcessingException
    {
        String[] fields = line.split(",");
        if (!"NULL".equals(fields[3]) && !("NULL".equals(fields[13]) && !("NULL".equals(fields[14]))))
        {
            DateTimeFormatter formatter =
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS")
                            .withZone(ZoneOffset.UTC);
            LocalDateTime ldt = LocalDateTime.parse(
                    fields[13].replace("\"", ""),
                    formatter
            );
            Double ask = Double.parseDouble(fields[3]);
            Instant feedDate = ldt.atZone(ZoneId.systemDefault()).toInstant();
            Integer instrumentId = Integer.parseInt(fields[14]);
            return LiveInstrumentRate.builder()
                    .ask(ask)
                    .instrumentId(instrumentId).date(feedDate).build();
        }

        return null;
    }

    public InputStream loadResource(String path)
    {
        return getClass().getResourceAsStream(path);
    }

}
