package com.hkcapital.portflio.service.marketconditions.impl;

import com.hkcapital.portflio.model.Instrument;
import com.hkcapital.portflio.model.MarketConditions;
import com.hkcapital.portflio.model.Strategy;
import com.hkcapital.portflio.repository.marketconditions.MarketConditionsRepository;
import com.hkcapital.portflio.service.marketconditions.MarketConditionsService;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MarketConditionsServiceImpl implements MarketConditionsService
{
    private final MarketConditionsRepository marketconRepo;

    public MarketConditionsServiceImpl(MarketConditionsRepository marketconditionsRepository)
    {
        this.marketconRepo = marketconditionsRepository;
    }

    @Override
    public MarketConditions addMarketCondition(MarketConditions marketCondition)
    {
        return marketconRepo.save(marketCondition);
    }

    @Override
    public void removeMarketCondition(MarketConditions marketCondition)
    {
        marketconRepo.delete(marketCondition);
    }

    @Override
    public Strategy updateMarketCondition(MarketConditions marketCondition)
    {
        return null;
    }

    @Override
    public void removeById(Integer id)
    {
        marketconRepo.findById(id) //
                .ifPresent(marketConditions -> marketconRepo.delete(marketConditions));
    }

    @Override
    public List<MarketConditions> findAll()
    {
        return marketconRepo.findAll(Sort.by(Sort.Direction.DESC, "id"));
    }

    @Override
    public MarketConditions findById(Integer id)
    {
        Optional<MarketConditions> marketCondition = marketconRepo.findById(id);
        return !marketCondition.isEmpty() ? marketCondition.get() : null;
    }

    @Override
    public void removeAll()
    {
        marketconRepo.deleteAll();
    }

    @Override
    public MarketConditions findReferenceById(Integer id) //
    {
        return marketconRepo.getReferenceById(id);
    }

    @Override
    public MarketConditions //
    findByInstrumentAndDayLowAndDayHighAndPercentMove(Instrument instrument,
                                                      Double dayLow,
                                                      Double dayHigh,
                                                      Double percentMove)
    {
        return marketconRepo. //
                findByInstrumentAndDayLowAndDayHighAndPercentMove(instrument, dayLow, dayHigh, percentMove);
    }
}
