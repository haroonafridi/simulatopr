package com.hkcapital.portflio.service.instrumentmarketstructureconf;

import com.hkcapital.portflio.model.Instrument;
import com.hkcapital.portflio.model.InstrumentMarketStructureConf;
import com.hkcapital.portflio.service.registry.Service;

import java.util.List;

public interface InstrumentMarketStructureConfService extends Service
{
    List<InstrumentMarketStructureConf> findAll();
    InstrumentMarketStructureConf findById(Integer id);
    InstrumentMarketStructureConf add(InstrumentMarketStructureConf instMrkConf);

    List<InstrumentMarketStructureConf> findByInstrumentAndActiveOrdeyByMarketOrder(Instrument instrument, boolean active);

    void remove(InstrumentMarketStructureConf instMrkConf);
}
