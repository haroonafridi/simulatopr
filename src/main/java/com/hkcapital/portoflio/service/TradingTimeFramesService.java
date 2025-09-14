package com.hkcapital.portoflio.service;

import com.hkcapital.portoflio.model.Instrument;
import com.hkcapital.portoflio.model.Strategy;
import com.hkcapital.portoflio.model.TradingTimeFrames;
import jakarta.persistence.criteria.CriteriaBuilder;

import java.util.List;

public interface TradingTimeFramesService<T> extends Service
{
    void add(TradingTimeFrames tradingTimeFrames);
    void remove(TradingTimeFrames tradingTimeFrames);

    TradingTimeFrames findById(Integer id);

    List<TradingTimeFrames> findAll();

}
