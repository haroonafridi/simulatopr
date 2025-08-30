package com.hkcapital.portoflio.service;

import com.hkcapital.portoflio.model.*;

import java.util.List;

public interface StrategyService
{
    Strategy addStrategy(Strategy strategy);

    void removeStrategy(Strategy strategy);

    Strategy updateStrategy(Strategy strategy);

    List<Strategy> findAll();

    Strategy findById(Integer id);

    void removeById(Integer id);

    void removeAll();

}
