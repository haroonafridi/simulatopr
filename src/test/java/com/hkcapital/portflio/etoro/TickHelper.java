package com.hkcapital.portflio.etoro;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hkcapital.portflio.market.indicators.Tick;
import com.hkcapital.portflio.service.api.etoro.websocket.LiveInstrumentRate;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class TickHelper
{
    public static LiveInstrumentRate rateFromString(final String line) throws JsonProcessingException
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

    public static Tick tickFromRate(final LiveInstrumentRate rate)
    {
        return Tick.builder().instrument(rate.getInstrumentId().toString()) //
                .time(rate.getDate())//
                .val(rate.getAsk()) //
                .build();
    }
}
