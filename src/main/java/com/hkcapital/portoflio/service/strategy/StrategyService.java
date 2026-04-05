package com.hkcapital.portoflio.service.strategy;

import com.hkcapital.portoflio.model.*;
import com.hkcapital.portoflio.service.registry.Service;

import java.util.List;

public interface StrategyService extends Service
{
    Strategy addStrategy(Strategy strategy);

    void removeStrategy(Strategy strategy);

    Strategy updateStrategy(Strategy strategy);

    List<Strategy> findAll();

    Strategy findById(Integer id);

    void removeById(Integer id);

    void removeAll();

}
