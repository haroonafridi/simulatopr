package com.hkcapital.portflio.service.marketconditions;

import com.hkcapital.portflio.model.Instrument;
import com.hkcapital.portflio.model.MarketConditions;
import com.hkcapital.portflio.model.Strategy;
import com.hkcapital.portflio.service.registry.Service;

import java.util.List;

public interface MarketConditionsService extends Service
{
    MarketConditions addMarketCondition(MarketConditions marketCondition);

    Strategy updateMarketCondition(MarketConditions marketCondition);

    List<MarketConditions> findAll();

    MarketConditions findById(Integer id);


    MarketConditions findReferenceById(Integer id);

    MarketConditions findByInstrumentAndDayLowAndDayHighAndPercentMove(Instrument instrument,
                                                                       Double dayLow,
                                                                       Double dayHigh,
                                                                       Double percentMove);

    void removeMarketCondition(MarketConditions marketCondition);
    void removeById(Integer id);
    void removeAll();
}
