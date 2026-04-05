package com.hkcapital.portoflio.service.instrument.impl;

import com.hkcapital.portoflio.model.Instrument;
import com.hkcapital.portoflio.repository.instrument.InstrumentRepository;
import com.hkcapital.portoflio.service.instrument.InstrumentService;
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
        instrumentRepository.delete(instrument);
    }

    @Override
    public Instrument updateInstrument(Instrument instrument)
    {
        return instrumentRepository.save(instrument);
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

    @Override
    public Instrument findByEtoroInstrumentId(Integer id)
    {
        return null;
    }
}
