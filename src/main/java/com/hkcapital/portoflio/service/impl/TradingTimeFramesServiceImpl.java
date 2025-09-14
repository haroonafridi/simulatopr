package com.hkcapital.portoflio.service.impl;

import com.hkcapital.portoflio.model.TradingTimeFrames;
import com.hkcapital.portoflio.repository.TradingTimeFramesRepository;
import com.hkcapital.portoflio.service.TradingTimeFramesService;

import java.util.List;

public class TradingTimeFramesServiceImpl implements TradingTimeFramesService<TradingTimeFrames>
{
   private final TradingTimeFramesRepository tradingTimeFramesRepository;

    public TradingTimeFramesServiceImpl(TradingTimeFramesRepository tradingTimeFramesRepository)
    {
        this.tradingTimeFramesRepository = tradingTimeFramesRepository;
    }

    @Override
    public void add(TradingTimeFrames tradingTimeFrames)
    {
        tradingTimeFramesRepository.save(tradingTimeFrames);
    }

    @Override
    public void remove(TradingTimeFrames tradingTimeFrames)
    {
        tradingTimeFramesRepository.delete(tradingTimeFrames);
    }

    @Override
    public TradingTimeFrames findById(Integer id)
    {
        return tradingTimeFramesRepository.findById(id).get();
    }

    @Override
    public List<TradingTimeFrames> findAll()
    {
        return tradingTimeFramesRepository.findAll();
    }
}
