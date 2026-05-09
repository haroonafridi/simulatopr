package com.hkcapital.portflio.service.instrument;

import com.hkcapital.portflio.model.Instrument;
import com.hkcapital.portflio.service.registry.Service;

import java.util.List;

public interface InstrumentService  extends Service
{
    Instrument addInstrument(Instrument instrument);

    void removeInstrument(Instrument instrument);

    Instrument updateInstrument(Instrument instrument);

    List<Instrument> findAll();

    Instrument findById(Integer id);

    void removeAll();

    void removeById(Integer id);

    Instrument findByEtoroInstrumentId(Integer id);

    List<Instrument> findByActive(Boolean active);


}
