package com.hkcapital.portoflio.service.impl;

import com.hkcapital.portoflio.model.Instrument;
import com.hkcapital.portoflio.model.Strategy;
import com.hkcapital.portoflio.repository.InstrumentRepository;
import com.hkcapital.portoflio.service.InstrumentService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    @Override
    public List<Instrument> findAll()
    {
        return instrumentRepository.findAll();
    }

    @Override
    public Instrument findById(Integer id)
    {
        Optional<Instrument>  instrument = instrumentRepository.findById(id);
        return !instrument.isEmpty() ? instrument.get() : null;
    }

    @Override
    public void removeAll()
    {
        instrumentRepository.deleteAll();
    }

    @Override
    public void removeById(Integer id)
    {
        instrumentRepository.findById(id).ifPresent(instrument -> instrumentRepository.delete(instrument));
    }
}
