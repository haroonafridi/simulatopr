package com.hkcapital.portflio.service.orders;

import com.hkcapital.portflio.service.api.etoro.websocket.LiveInstrumentRate;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class InstrumentRateValidator
{
    public static boolean isInValidInstrumentRate(final LiveInstrumentRate instrumentRate)
    {
        if (instrumentRate == null || instrumentRate.getAsk() == null || instrumentRate.getBid() == null)
        {
            log.info("Unusual bid and ask received, cannot process order");
            return true;
        }
        return false;
    }
}
