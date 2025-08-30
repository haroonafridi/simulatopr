package com.hkcapital.portoflio.service.impl;

import com.hkcapital.portoflio.model.MarketConditions;
import com.hkcapital.portoflio.model.Strategy;
import com.hkcapital.portoflio.repository.MarketconditionsRepository;
import com.hkcapital.portoflio.service.MarketConditionsService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MarketConditionsServiceImpl implements MarketConditionsService
{
    private final MarketconditionsRepository marketconditionsRepository;

    public MarketConditionsServiceImpl(MarketconditionsRepository marketconditionsRepository)
    {
        this.marketconditionsRepository = marketconditionsRepository;
    }

    @Override
    public MarketConditions addMarketCondition(MarketConditions marketCondition)
    {
        return marketconditionsRepository.save(marketCondition);
    }

    @Override
    public void removeMarketCondition(MarketConditions marketCondition)
    {
        marketconditionsRepository.delete(marketCondition);
    }

    @Override
    public Strategy updateMarketCondition(MarketConditions marketCondition)
    {
        return null;
    }

    @Override
    public void removeById(Integer id)
    {
        marketconditionsRepository.findById(id) //
                .ifPresent(marketConditions -> marketconditionsRepository.delete(marketConditions));
    }

    @Override
    public List<MarketConditions> findAll()
    {
        return marketconditionsRepository.findAll();
    }

    @Override
    public MarketConditions findById(Integer id)
    {
        Optional<MarketConditions> marketCondition = marketconditionsRepository.findById(id);
        return !marketCondition.isEmpty() ? marketCondition.get() : null;
    }

    @Override
    public void removeAll()
    {
        marketconditionsRepository.deleteAll();
    }
}
