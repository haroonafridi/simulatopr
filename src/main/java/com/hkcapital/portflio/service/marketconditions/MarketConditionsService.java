package com.hkcapital.portflio.service.marketconditions;

import com.hkcapital.portflio.model.MarketConditions;
import com.hkcapital.portflio.model.Strategy;
import com.hkcapital.portflio.service.registry.Service;

import java.util.List;

public interface MarketConditionsService extends Service
{
    MarketConditions addMarketCondition(MarketConditions marketCondition);

    void removeMarketCondition(MarketConditions marketCondition);

    Strategy updateMarketCondition(MarketConditions marketCondition);

    void removeById(Integer id);

    List<MarketConditions> findAll();

    MarketConditions findById(Integer id);
    void removeAll();

    MarketConditions getReferenceById(Integer id);

}
