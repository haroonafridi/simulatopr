package com.hkcapital.portflio.service.orders;

import com.hkcapital.portflio.model.Instrument;
import com.hkcapital.portflio.service.api.etoro.websocket.LiveInstrumentRate;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SlippageValidator
{
    public static boolean isInValidSlippage(final LiveInstrumentRate instrumentRate, final Instrument instrument)
    {
        if (instrumentRate == null
                || instrumentRate.getAsk() == null
                || instrumentRate.getBid() == null
                || instrument.getMaxSlippage() == null)
        {
            log.info("Invalid instrument rate detected or no slippage configured." +
                            " ask = {} , bid = {} , configured slippage = {}, abort processing!",
                    instrumentRate.getAsk(),
                    instrumentRate.getBid(),
                    instrument.getMaxSlippage());
            return true;
        }

        final double ask = instrumentRate.getAsk();
        final double bid = instrumentRate.getBid();
        final double slippage = Math.abs(ask - bid);

        if (slippage > instrument.getMaxSlippage())
        {
            log.info("Unusual price detected cannot process order slippage = {} , max allowed slippage = {} ", //
                    slippage, instrument.getMaxSlippage());
            return true;
        }

        return false;
    }
}
