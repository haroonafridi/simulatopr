package com.hkcapital.portflio.service.tradingindicators.impl;

import com.hkcapital.portflio.model.Instrument;
import com.hkcapital.portflio.model.TradingIndicator;
import com.hkcapital.portflio.repository.tradingindicators.TradingIndicatorSpecification;
import com.hkcapital.portflio.repository.tradingindicators.TradingIndicatorsFilter;
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
    public TradingIndicator save(TradingIndicator indicators)
    {
        return indicatorsRepository.save(indicators);
    }

    @Override
    public List<TradingIndicator> findByName(String name)
    {
        return indicatorsRepository.findByName(name);
    }

    @Override
    public List<TradingIndicator> findByInstrument(Instrument instrument)
    {
        return indicatorsRepository.findByInstrument(instrument);
    }

    @Override
    public List<TradingIndicator> findAll()
    {
        return indicatorsRepository.findAll();
    }

    @Override
    public TradingIndicator findById(Integer id)
    {
        return indicatorsRepository.findById(id).get();
    }

    @Override
    public void remove(TradingIndicator indicators)
    {
        indicatorsRepository.delete(indicators);
    }

    @Override
    public void removeAll()
    {
        indicatorsRepository.deleteAll();
    }

    @Override
    public List<TradingIndicator> findByFilter(TradingIndicatorsFilter filter)
    {
        return indicatorsRepository.findAll(TradingIndicatorSpecification.byFilter(filter));
    }
}
