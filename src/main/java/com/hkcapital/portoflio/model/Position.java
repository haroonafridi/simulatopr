package com.hkcapital.portoflio.model;

public class Position

{
    private Integer index;
    private Instrument instrument;
    private Double percentCapitalDeployed;

    public Position(Integer index, Instrument instrument, Double percentCapitalDeployed)
    {
        this.index = index;
        this.instrument = instrument;
        this.percentCapitalDeployed = percentCapitalDeployed;
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

    public Double getPercentCapitalDeployed()
    {
        return percentCapitalDeployed;
    }

    public void setPercentCapitalDeployed(Double percentCapitalDeployed)
    {
        this.percentCapitalDeployed = percentCapitalDeployed;
    }
}
