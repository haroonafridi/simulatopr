package com.hkcapital.portoflio.etoro;

public enum Instruments
{
    BTC(219000),
    GOLD(207010);

    private Integer instrumentId;

    Instruments(Integer instrumentId)
    {
        this.instrumentId = instrumentId;
    }

    public Integer getInstrumentId()
    {
        return instrumentId;
    }
}
