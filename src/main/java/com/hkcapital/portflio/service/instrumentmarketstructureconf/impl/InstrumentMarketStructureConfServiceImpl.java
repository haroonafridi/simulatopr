package com.hkcapital.portflio.service.instrumentmarketstructureconf.impl;

import com.hkcapital.portflio.model.Instrument;
import com.hkcapital.portflio.model.InstrumentMarketStructureConf;
import com.hkcapital.portflio.repository.instrumentmarketstructureconf.InstrumentMarketStructureConfRepository;
import com.hkcapital.portflio.service.instrumentmarketstructureconf.InstrumentMarketStructureConfService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InstrumentMarketStructureConfServiceImpl //
        implements InstrumentMarketStructureConfService
{
    private final InstrumentMarketStructureConfRepository instMrkConfRepo;

    public InstrumentMarketStructureConfServiceImpl(InstrumentMarketStructureConfRepository instMrkConfRepo)
    {
        this.instMrkConfRepo = instMrkConfRepo;
    }

    @Override
    public InstrumentMarketStructureConf add(InstrumentMarketStructureConf instMrkConf)
    {
        return instMrkConfRepo.save(instMrkConf);
    }

    @Override
    public List<InstrumentMarketStructureConf> findAll()
    {
        return instMrkConfRepo.findAll();
    }

    @Override
    public InstrumentMarketStructureConf findById(Integer id)
    {
        return instMrkConfRepo.findById(id).orElseThrow();
    }

    @Override
    public List<InstrumentMarketStructureConf> //
    findByInstrumentAndActiveOrdeyByMarketOrder(Instrument instrument, boolean active)
    {
        return instMrkConfRepo.findByInstrumentAndActiveOrderByMarketOrder(instrument, active);
    }

    @Override
    public void remove(InstrumentMarketStructureConf instMrkConf)
    {
        instMrkConfRepo.delete(instMrkConf);
    }
}
