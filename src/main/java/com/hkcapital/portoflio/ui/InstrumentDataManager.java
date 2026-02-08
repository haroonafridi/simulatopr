package com.hkcapital.portoflio.ui;

public interface InstrumentDataManager
{
    public String getInstrumentCandleData(Integer instrument,
                                          String sortOrder,
                                          String timeInterval,
                                          Integer pages);
}
