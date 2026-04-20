package com.hkcapital.portoflio.indicators;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.time.Instant;

@Builder
@AllArgsConstructor
@Data
@ToString
public class Candle
{
    private String instrument;
    private double open;
    private double low;
    private double high;
    private double close;
    private Instant time;

    private Unit unit;

    private Integer interval;

    public String getInstrument()
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

    public Unit getUnit()
    {
        return unit;
    }

    public Integer getInterval()
    {
        return interval;
    }
}
