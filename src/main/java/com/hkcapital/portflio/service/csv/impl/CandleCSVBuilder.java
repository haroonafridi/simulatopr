package com.hkcapital.portflio.service.csv.impl;

import com.hkcapital.portflio.model.Candle;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.StringJoiner;

public class CandleCSVBuilder
{

    private static final DateTimeFormatter INSTANT_FMT = DateTimeFormatter.ISO_INSTANT;
    private static final DateTimeFormatter LOCAL_DT_FMT = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    public static String buildHeader()
    {
        return String.join(",",
                "id",
                "instrument_id",
                "from_date",
                "open",
                "high",
                "high_time",
                "low",
                "low_time",
                "close",
                "volume",
                "time_frame",
                "time_frame_unit",
                "source",
                "source_time_frame",
                "atr",
                "rsi",
                "ema",
                "sma",
                "creation_date_time"
        );
    }

    public static String buildRow(Candle c)
    {
        StringJoiner sj = new StringJoiner(",");

        sj.add(String.valueOf(c.getId()));
        sj.add(String.valueOf(c.getInstrumentID()));
        sj.add(formatInstant(c.getFromDate()));
        sj.add(String.valueOf(c.getOpen()));
        sj.add(String.valueOf(c.getHigh()));
        sj.add(formatInstant(c.getHighTime()));
        sj.add(String.valueOf(c.getLow()));
        sj.add(formatInstant(c.getLowTime()));
        sj.add(String.valueOf(c.getClose()));
        sj.add(String.valueOf(c.getVolume()));
        sj.add(nullSafe(c.getTimeFrame()));
        sj.add(nullSafe(c.getTimeFrameUnit()));
        sj.add(nullSafe(c.getSource()));
        sj.add(nullSafe(c.getSourceTimeFrame()));
        sj.add(nullSafe(c.getAtr()));
        sj.add(nullSafe(c.getRsi()));
        sj.add(nullSafe(c.getEma()));
        sj.add(nullSafe(c.getSma()));
        sj.add(formatLocalDateTime(c.getCreationDateTime()));

        return sj.toString();
    }

    public static String buildCSV(List<Candle> candles)
    {
        StringBuilder sb = new StringBuilder();
        sb.append(buildHeader()).append("\n");

        for (Candle c : candles)
        {
            sb.append(buildRow(c)).append("\n");
        }

        return sb.toString();
    }

    private static String formatInstant(java.time.Instant instant)
    {
        return instant != null ? INSTANT_FMT.format(instant) : "";
    }

    private static String formatLocalDateTime(java.time.LocalDateTime dt)
    {
        return dt != null ? LOCAL_DT_FMT.format(dt) : "";
    }

    private static String nullSafe(Object o)
    {
        return o != null ? String.valueOf(o) : "";
    }
}