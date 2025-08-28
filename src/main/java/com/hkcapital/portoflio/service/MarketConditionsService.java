package com.hkcapital.portoflio.service;

import com.hkcapital.portoflio.model.MarketConditions;
import com.hkcapital.portoflio.model.Strategy;

public interface MarketConditionsService
{
    MarketConditions addMarketCondition(MarketConditions marketCondition);

    void removeMarketCondition(MarketConditions marketCondition);

    Strategy updateMarketCondition(MarketConditions marketCondition);

}
