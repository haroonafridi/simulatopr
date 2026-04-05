package com.hkcapital.portoflio.service.tradingsessions.impl;

import com.hkcapital.portoflio.model.TradingSessions;
import com.hkcapital.portoflio.repository.tradingsession.TradingSessionsRepository;
import com.hkcapital.portoflio.service.tradingsessions.TradingSessionsService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TradingSessionsServiceImpl implements TradingSessionsService<TradingSessions>
{
    private final TradingSessionsRepository tradingSessionsRepository;

    public TradingSessionsServiceImpl(TradingSessionsRepository tradingSessionsRepository)
    {
        this.tradingSessionsRepository = tradingSessionsRepository;
    }

    @Override
    public void add(TradingSessions tradingSessions)
    {
        tradingSessionsRepository.save(tradingSessions);
    }

    @Override
    public void remove(TradingSessions tradingSessions)
    {
        tradingSessionsRepository.delete(tradingSessions);
    }

    @Override
    public TradingSessions findById(Integer id)
    {
        Optional<TradingSessions> tradingSessions = tradingSessionsRepository.findById(id);

        if (tradingSessions.isPresent())
        {
            return tradingSessions.get();
        }
        return null;
    }

    @Override
    public List<TradingSessions> findAll()
    {
        return tradingSessionsRepository.findAll();
    }
}
