package com.hkcapital.portflio.service.tradingindicators.impl;

import com.hkcapital.portflio.model.Instrument;
import com.hkcapital.portflio.model.TradingIndicators;
import com.hkcapital.portflio.repository.tradingindicators.TradingIndicatorsRepository;
import com.hkcapital.portflio.service.tradingindicators.TradingIndicatorsService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TradingIndicatorsServiceImpl implements TradingIndicatorsService
{
    private final TradingIndicatorsRepository indicatorsRepository;

    public TradingIndicatorsServiceImpl(TradingIndicatorsRepository indicatorsRepository)
    {
        this.indicatorsRepository = indicatorsRepository;
    }

    @Override
    public TradingIndicators save(TradingIndicators indicators)
    {
        return indicatorsRepository.save(indicators);
    }

    @Override
    public List<TradingIndicators> findByName(String name)
    {
        return indicatorsRepository.findByName(name);
    }

    @Override
    public List<TradingIndicators> findByInstrument(Instrument instrument)
    {
        return indicatorsRepository.findByInstrument(instrument);
    }

    @Override
    public List<TradingIndicators> findAll()
    {
        return indicatorsRepository.findAll();
    }

    @Override
    public TradingIndicators findById(Integer id)
    {
        return indicatorsRepository.findById(id).get();
    }

    @Override
    public void remove(TradingIndicators indicators)
    {
        indicatorsRepository.delete(indicators);
    }
}
