package com.hkcapital.portoflio.service;

import com.hkcapital.portoflio.model.Position;
import com.hkcapital.portoflio.model.Strategy;

public interface PositionService
{
    Position addPosition(Position position);

    void removePosition(Position position);

    Position updatePosition(Position position);


}
