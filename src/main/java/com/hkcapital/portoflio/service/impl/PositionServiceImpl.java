package com.hkcapital.portoflio.service.impl;

import com.hkcapital.portoflio.model.Position;
import com.hkcapital.portoflio.repository.PositionRepository;
import com.hkcapital.portoflio.service.PositionService;
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
    public void removeAll()
    {
        positionPnLRepository.deleteAll();
    }
}
