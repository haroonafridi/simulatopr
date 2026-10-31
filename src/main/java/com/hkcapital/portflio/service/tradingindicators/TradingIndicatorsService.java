package com.hkcapital.portflio.service.tradingindicators;

import com.hkcapital.portflio.model.Instrument;
import com.hkcapital.portflio.model.TradingIndicators;
import com.hkcapital.portflio.service.registry.Service;

import java.util.List;

public interface TradingIndicatorsService extends Service
{
    TradingIndicators save(TradingIndicators indicators);

    List<TradingIndicators> findByName(String name);

    List<TradingIndicators> findByInstrument(Instrument instrument);
    List<TradingIndicators> findAll();

    TradingIndicators findById(Integer id);
    void remove(TradingIndicators indicators);

}
