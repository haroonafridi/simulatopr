package com.hkcapital.portflio.market.indicators;

import com.hkcapital.portflio.model.Instrument;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.time.Instant;

@Builder
@AllArgsConstructor
@Data
@ToString
public class CandleDto
{
    private Instrument instrument;
    private double open;
    private double low;
    private Instant lowTime;
    private double high;
    private Instant highTime;
    private double close;
    private Instant time;

    private TimeFramesUnit timeFramesUnit;

    private Integer interval;

    public Instrument getInstrument()
    {
        return instrument;
    }

    public double getOpen()
    {
        return open;
    }

    public double getLow()
    {
        return low;
    }

    public double getHigh()
    {
        return high;
    }

    public double getClose()
    {
        return close;
    }

    public Instant getTime()
    {
        return time;
    }

    public TimeFramesUnit getTimeFramesUnit()
    {
        return timeFramesUnit;
    }

    public Integer getInterval()
    {
        return interval;
    }
}
