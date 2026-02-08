package com.hkcapital.portoflio.service.impl;

import com.hkcapital.portoflio.model.Configuration;
import com.hkcapital.portoflio.model.MarketConditions;
import com.hkcapital.portoflio.model.Position;
import com.hkcapital.portoflio.repository.PositionRepository;
import com.hkcapital.portoflio.service.PositionService;
import com.hkcapital.portoflio.ui.panels.position.PositionParameters;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class PositionServiceImpl implements PositionService
{

    private final PositionRepository positionPnLRepository;

    public PositionServiceImpl(PositionRepository positionPnLRepository)
    {
        this.positionPnLRepository = positionPnLRepository;
    }

    @Override
    public Position add(Position position)
    {
        return positionPnLRepository.save(position);
    }

    @Override
    public void remove(Position position)
    {
        positionPnLRepository.delete(position);
    }

    @Override
    public Position updatePosition(Position position)
    {
        return positionPnLRepository.save(position);
    }

    @Override
    public Position findById(Integer id)
    {
        Optional<Position> positionPnL = positionPnLRepository.findById(id);
        return positionPnL.isEmpty() ? null : positionPnL.get();
    }

    @Override
    public List<Position> findAll()
    {
        return positionPnLRepository.findAll();
    }

    @Override
    public void removeAll(List<Position> positionList)
    {
        positionPnLRepository.deleteAll(positionList);
    }

    @Override
    public List<Position> findByStrategyId(Integer id)
    {
        return positionPnLRepository.findByStrategyId(id);
    }


    public static PositionParameters calculatePosition(List<Position> positionList, Configuration configuration,
                                                       MarketConditions marketConditions,
                                                       Double positionSizeInPercent,
                                                       Double openingCapital,
                                                       Double allocatedCapital)
    {

        Double capital = allocatedCapital * positionSizeInPercent / 100;
        Double percentCapitalDeployed = (capital / openingCapital) * 100;
        Double percentPnl = 0d;
        Double pnl = 0d;
        Double allowedFirePower = allocatedCapital;
        Double remainingCapital = openingCapital - capital;

        Double remainingFirePower = allowedFirePower - capital;

        System.out.println("percentCapitalDeployed => "+percentCapitalDeployed+ "capital:  "+capital+ " remainingCapital => "+remainingCapital);

        return new PositionParameters(percentCapitalDeployed, capital, pnl, percentPnl, allowedFirePower,
                remainingFirePower , remainingCapital);
    }

}
