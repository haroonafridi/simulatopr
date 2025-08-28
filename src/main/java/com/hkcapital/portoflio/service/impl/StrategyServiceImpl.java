package com.hkcapital.portoflio.service.impl;

import com.hkcapital.portoflio.model.*;
import com.hkcapital.portoflio.repository.StrategyRepository;
import com.hkcapital.portoflio.service.StrategyService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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

}
