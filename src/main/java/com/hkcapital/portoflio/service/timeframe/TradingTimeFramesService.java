package com.hkcapital.portoflio.service.timeframe;

import com.hkcapital.portoflio.model.TradingTimeFrames;
import com.hkcapital.portoflio.service.registry.Service;

import java.util.List;

public interface TradingTimeFramesService<T> extends Service
{
    void add(TradingTimeFrames tradingTimeFrames);
    void remove(TradingTimeFrames tradingTimeFrames);

    TradingTimeFrames findById(Integer id);

    List<TradingTimeFrames> findAll();

}
