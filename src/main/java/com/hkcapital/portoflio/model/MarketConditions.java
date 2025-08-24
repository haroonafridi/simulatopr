package com.hkcapital.portoflio.model;

public class MarketConditions
{
    Integer index;
    Instrument instrument;
    Double dayLow;
    Double dayHigh;
    Double percentMove;

    public MarketConditions(Integer index, Instrument instrument, Double dayLow, Double dayHigh, Double percentMove)
    {
        this.index = index;
        this.instrument = instrument;
        this.dayLow = dayLow;
        this.dayHigh = dayHigh;
        this.percentMove = percentMove;
    }

    public Integer getIndex()
    {
        return index;
    }

    public void setIndex(Integer index)
    {
        this.index = index;
    }

    public Instrument getInstrument()
    {
        return instrument;
    }

    public void setInstrument(Instrument instrument)
    {
        this.instrument = instrument;
    }

    public Double getDayLow()
    {
        return dayLow;
    }

    public void setDayLow(Double dayLow)
    {
        this.dayLow = dayLow;
    }

    public Double getDayHigh()
    {
        return dayHigh;
    }

    public void setDayHigh(Double dayHigh)
    {
        this.dayHigh = dayHigh;
    }

    public Double getPercentMove()
    {
        return percentMove;
    }

    public void setPercentMove(Double percentMove)
    {
        this.percentMove = percentMove;
    }
}
