package com.hkcapital.portoflio.indicators;

import lombok.Builder;

import java.time.Instant;

@Builder
public class Tick
{
    private String instrument;
    private double val;
    private Instant time;

    public double getVal()
    {
        return val;
    }

    public void setVal(double val)
    {
        this.val = val;
    }

    public Instant getTime()
    {
        return time;
    }

    public void setTime(Instant time)
    {
        this.time = time;
    }

    public String getInstrument()
    {
        return instrument;
    }
}
