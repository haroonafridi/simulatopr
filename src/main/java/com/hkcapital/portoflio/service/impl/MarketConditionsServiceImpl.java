package com.hkcapital.portoflio.service.impl;

import com.hkcapital.portoflio.model.MarketConditions;
import com.hkcapital.portoflio.model.Strategy;
import com.hkcapital.portoflio.repository.MarketconditionsRepository;
import com.hkcapital.portoflio.service.MarketConditionsService;
import org.springframework.stereotype.Service;

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

    }

    @Override
    public Strategy updateMarketCondition(MarketConditions marketCondition)
    {
        return null;
    }
}
