package com.hkcapital.portflio.service.marketstructure;

import com.hkcapital.portflio.model.InstrumentMarketStructure;
import com.hkcapital.portflio.service.registry.Service;

import java.util.List;

public interface InstrumentMarketStructureService extends Service
{
    InstrumentMarketStructure add(InstrumentMarketStructure InstMarkStrctre);

    List<InstrumentMarketStructure> finalAll();

    List<InstrumentMarketStructure> findByMarketStructureKey(String marketStructureKey);
}
