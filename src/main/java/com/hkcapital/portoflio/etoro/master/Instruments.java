package com.hkcapital.portoflio.etoro.master;

public enum Instruments
{
    BTC(100000),
    GOLD(18),
    NASDAQ1100(28);

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
