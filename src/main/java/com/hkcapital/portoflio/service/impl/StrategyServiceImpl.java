package com.hkcapital.portoflio.service.impl;

import com.hkcapital.portoflio.model.*;
import com.hkcapital.portoflio.repository.StrategyRepository;
import com.hkcapital.portoflio.service.StrategyService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class StrategyServiceImpl implements StrategyService
{
    private final StrategyRepository strategyRepository;

    public StrategyServiceImpl(StrategyRepository strategyRepository)
    {
        this.strategyRepository = strategyRepository;
    }

    @Override
    public Strategy addStrategy(Strategy strategy)
    {
        return strategyRepository.save(strategy);
    }

    @Override
    public void removeStrategy(Strategy strategy)
    {
        strategyRepository.delete(strategy);
    }

    @Override
    public Strategy updateStrategy(Strategy strategy)
    {
         return strategyRepository.save(strategy);
    }

    @Override
    public List<Strategy> findAll()
    {
        return strategyRepository.findAll();
    }

    @Override
    public Strategy findById(Integer id)
    {
        final Optional<Strategy> strategy = strategyRepository.findById(id);
        return !strategy.isEmpty() ? strategy.get() : null;
    }

    @Override
    public void removeById(Integer id)
    {
        strategyRepository.findById(id).ifPresent(strategy -> strategyRepository.delete(strategy));
    }

    @Override
    public void removeAll()
    {
        strategyRepository.deleteAll();
    }

}
