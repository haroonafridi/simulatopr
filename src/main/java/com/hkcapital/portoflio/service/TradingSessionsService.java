package com.hkcapital.portoflio.service;

import com.hkcapital.portoflio.model.TradingSessions;
import com.hkcapital.portoflio.model.TradingTimeFrames;

import java.util.List;

public interface TradingSessionsService<T> extends Service
{
    void add(TradingSessions tradingSessions);
    void remove(TradingSessions tradingSessions);

    TradingSessions findById(Integer id);

    List<TradingSessions> findAll();

}
