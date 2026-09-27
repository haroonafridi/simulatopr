package com.hkcapital.portflio.service.orders;

import com.hkcapital.portflio.model.Instrument;
import com.hkcapital.portflio.service.api.etoro.websocket.LiveInstrumentRate;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SlippageValidator
{
    public static boolean isInValidSlippage(final LiveInstrumentRate instrumentRate, final Instrument instrument)
    {
        final Double ask = instrumentRate.getAsk();
        final Double bid = instrumentRate.getBid();
        final Double slippage = Math.abs(ask - bid);

        if (slippage > instrument.getMaxSlippage())
        {
            log.info("Unusual price detected cannot process order slippage = {} , max allowed slippage = {} ", //
                    slippage, instrument.getMaxSlippage());
            return true;
        }

        return false;
    }
}
