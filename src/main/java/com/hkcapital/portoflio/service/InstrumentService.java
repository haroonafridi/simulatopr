package com.hkcapital.portoflio.service;

import com.hkcapital.portoflio.model.Instrument;
import com.hkcapital.portoflio.model.Strategy;

import java.util.List;

public interface InstrumentService  extends Service
{
    Instrument addInstrument(Instrument instrument);

    void removeInstrument(Instrument instrument);

    Strategy updateInstrument(Instrument instrument);

    List<Instrument> findAll();

    Instrument findById(Integer id);

    void removeAll();

    void removeById(Integer id);


}
