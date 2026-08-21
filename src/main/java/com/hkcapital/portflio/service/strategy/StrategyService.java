package com.hkcapital.portflio.service.strategy;

import com.hkcapital.portflio.model.*;
import com.hkcapital.portflio.service.registry.Service;

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

    Strategy findByIdOrderByActiveDesc(Integer id);

}
