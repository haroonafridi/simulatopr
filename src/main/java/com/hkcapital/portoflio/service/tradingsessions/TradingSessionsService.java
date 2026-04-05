package com.hkcapital.portoflio.service.tradingsessions;

import com.hkcapital.portoflio.model.TradingSessions;
import com.hkcapital.portoflio.service.registry.Service;

import java.util.List;

public interface TradingSessionsService<T> extends Service
{
    void add(TradingSessions tradingSessions);
    void remove(TradingSessions tradingSessions);

    TradingSessions findById(Integer id);

    List<TradingSessions> findAll();

}
