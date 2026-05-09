package com.hkcapital.portflio.ui;

public interface InstrumentDataService
{
    public String getInstrumentCandleData(Integer instrument,
                                          String sortOrder,
                                          String timeInterval,
                                          Integer pages);
}
