package com.hkcapital.portflio.service.positions;

import com.hkcapital.portflio.model.Position;
import com.hkcapital.portflio.service.registry.Service;

import java.util.List;

public interface PositionService extends Service
{
    Position add(Position positionPnL);

    void remove(Position positionPnL);

    Position updatePosition(Position positionPnL);

    Position findById(Integer id);

    List<Position> findAll();


    List<Position> findByStrategyId(Integer id);


    void removeAll(List<Position> positionList);

    void removeAll();


    List<Position> findByStrategyIdOrderByActive(Integer id, boolean active);

    List<Position> findByStrategyIdAndActivePositionsOrderByActive(Integer id, boolean active);

}
