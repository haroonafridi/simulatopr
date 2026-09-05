package com.hkcapital.portflio.service.instrument.impl;

import com.hkcapital.portflio.model.Instrument;
import com.hkcapital.portflio.repository.instrument.InstrumentRepository;
import com.hkcapital.portflio.service.instrument.InstrumentService;
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
    public List<Instrument> findAllOrderByName()
    {
        return instrumentRepository.findAllByOrderByNameAsc();
    }

    @Override
    public Instrument findByName(String name)
    {
        return instrumentRepository.findByName(name);
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
        return instrumentRepository.findByEtoroInstrumentId(id);
    }

    @Override
    public List<Instrument> findByActive(Boolean active)
    {
        return instrumentRepository.findByActive(active);
    }

    @Override
    public List<Instrument> findByActiveAndWithCandle(Boolean active, Boolean withCandle)
    {
        return instrumentRepository.findByActiveAndWithCandle(active, withCandle);
    }

    @Override
    public Instrument findByInstrumentTicker(String instrumentTicker)
    {
        return instrumentRepository.findByInstrumentTicker(instrumentTicker);
    }

    @Override
    public List<Instrument> findByActiveAndWithFeed(Boolean active, Boolean withFeed)
    {
        return instrumentRepository.findByActiveAndWithFeed(active, withFeed);
    }

    @Override
    public List<Instrument> findByActiveAndWithBand(Boolean active, Boolean withBand)
    {
        return instrumentRepository.findByActiveAndWithBand(active, withBand);
    }



}
