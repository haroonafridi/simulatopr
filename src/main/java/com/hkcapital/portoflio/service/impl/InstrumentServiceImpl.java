package com.hkcapital.portoflio.service.impl;

import com.hkcapital.portoflio.model.Instrument;
import com.hkcapital.portoflio.model.Strategy;
import com.hkcapital.portoflio.repository.InstrumentRepository;
import com.hkcapital.portoflio.service.InstrumentService;
import org.springframework.stereotype.Service;

@Service
public class InstrumentServiceImpl implements InstrumentService
{

    private final InstrumentRepository instrumentRepository;

    public InstrumentServiceImpl(InstrumentRepository instrumentRepository)
    {
        this.instrumentRepository = instrumentRepository;
    }

    @Override
    public Instrument addInstrument(Instrument instrument)
    {
        return instrumentRepository.save(instrument);
    }

    @Override
    public void removeInstrument(Instrument instrument)
    {

    }

    @Override
    public Strategy updateInstrument(Instrument instrument)
    {
        return null;
    }
}
