package com.hkcapital.portoflio.service;

import com.hkcapital.portoflio.model.Instrument;
import com.hkcapital.portoflio.model.Strategy;

public interface InstrumentService
{
    Instrument addInstrument(Instrument instrument);

    void removeInstrument(Instrument instrument);

    Strategy updateInstrument(Instrument instrument);


}
