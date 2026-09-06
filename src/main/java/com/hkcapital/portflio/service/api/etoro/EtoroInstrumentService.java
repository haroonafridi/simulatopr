package com.hkcapital.portflio.service.api.etoro;

import com.hkcapital.portflio.broker.etoro.instrument.EtoroInstrument;
import com.hkcapital.portflio.broker.etoro.instrument.InstrumentResponse;
import com.hkcapital.portflio.service.registry.Service;

public interface EtoroInstrumentService extends Service
{
    InstrumentResponse fetchInstrumentInstrumentResponse(String ticker);

    EtoroInstrument fetchInstrumentDetails(String ticker);

    Integer fetchInstrumentId(String ticker);
}
