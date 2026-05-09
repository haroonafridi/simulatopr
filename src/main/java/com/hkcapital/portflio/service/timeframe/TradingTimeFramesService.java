package com.hkcapital.portflio.service.timeframe;

import com.hkcapital.portflio.model.TradingTimeFrames;
import com.hkcapital.portflio.service.registry.Service;

import java.util.List;

public interface TradingTimeFramesService<T> extends Service
{
    void add(TradingTimeFrames tradingTimeFrames);
    void remove(TradingTimeFrames tradingTimeFrames);

    TradingTimeFrames findById(Integer id);

    List<TradingTimeFrames> findAll();

}
