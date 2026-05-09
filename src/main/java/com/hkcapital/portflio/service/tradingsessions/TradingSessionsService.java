package com.hkcapital.portflio.service.tradingsessions;

import com.hkcapital.portflio.model.TradingSessions;
import com.hkcapital.portflio.service.registry.Service;

import java.util.List;

public interface TradingSessionsService<T> extends Service
{
    void add(TradingSessions tradingSessions);
    void remove(TradingSessions tradingSessions);

    TradingSessions findById(Integer id);

    List<TradingSessions> findAll();

}
