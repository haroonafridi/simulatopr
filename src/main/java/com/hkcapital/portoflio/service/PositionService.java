package com.hkcapital.portoflio.service;

import com.hkcapital.portoflio.model.Position;

import java.util.List;

public interface PositionService
{
    Position add(Position positionPnL);

    void remove(Position positionPnL);

    Position updatePosition(Position positionPnL);

    Position findById(Integer id);

    List<Position> findAll();


    List<Position> findByStrategyId(Integer id);


    void removeAll(List<Position> positionList);


}
