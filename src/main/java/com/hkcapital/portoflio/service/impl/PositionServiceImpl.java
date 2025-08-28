package com.hkcapital.portoflio.service.impl;

import com.hkcapital.portoflio.model.Position;
import com.hkcapital.portoflio.repository.PositionRepository;
import com.hkcapital.portoflio.service.PositionService;
import org.springframework.stereotype.Service;

@Service
public class PositionServiceImpl implements PositionService
{

    private final PositionRepository positionRepository;

    public PositionServiceImpl(PositionRepository positionRepository)
    {
        this.positionRepository = positionRepository;
    }

    @Override
    public Position addPosition(Position position)
    {
        return positionRepository.save(position);
    }

    @Override
    public void removePosition(Position position)
    {
        positionRepository.delete(position);
    }

    @Override
    public Position updatePosition(Position position)
    {
        return   positionRepository.save(position);
    }
}
