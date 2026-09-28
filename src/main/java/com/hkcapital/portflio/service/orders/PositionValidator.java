package com.hkcapital.portflio.service.orders;

import com.hkcapital.portflio.model.Instrument;
import com.hkcapital.portflio.model.Position;
import com.hkcapital.portflio.service.api.etoro.websocket.LiveInstrumentRate;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PositionValidator
{
    public static boolean isInValidPosition(final Position position)
    {

        if(position.getPositionType() == null)
        {
            log.info("Empty position type found cannot send order!");
            return true;
        }

        if (position.getExecutionCount() == null || position.getExecutionCount() <= 0)
        {
            return true;
        }
        return false;
    }
}
