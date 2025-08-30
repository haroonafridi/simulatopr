package com.hkcapital.portoflio.service;

import com.hkcapital.portoflio.model.MarketConditions;
import com.hkcapital.portoflio.model.Strategy;

import java.util.List;

public interface MarketConditionsService
{
    MarketConditions addMarketCondition(MarketConditions marketCondition);

    void removeMarketCondition(MarketConditions marketCondition);

    Strategy updateMarketCondition(MarketConditions marketCondition);

    void removeById(Integer id);

    List<MarketConditions> findAll();

    MarketConditions findById(Integer id);
    void removeAll();

}
