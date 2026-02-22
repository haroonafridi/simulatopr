package com.hkcapital.portoflio.ui;

public interface InstrumentDataService
{
    public String getInstrumentCandleData(Integer instrument,
                                          String sortOrder,
                                          String timeInterval,
                                          Integer pages);
}
