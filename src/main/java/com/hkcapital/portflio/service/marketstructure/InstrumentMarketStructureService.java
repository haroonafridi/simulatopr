package com.hkcapital.portflio.service.marketstructure;

import com.hkcapital.portflio.model.InstrumentMarketStructure;

import java.util.List;

public interface InstrumentMarketStructureService
{
    InstrumentMarketStructure add(InstrumentMarketStructure InstMarkStrctre);

    List<InstrumentMarketStructure> finalAll();
}
