package com.hkcapital.portflio.service.tradingindicators;

import com.hkcapital.portflio.model.Instrument;
import com.hkcapital.portflio.model.SRMatrix;
import com.hkcapital.portflio.model.TradingIndicator;
import com.hkcapital.portflio.repository.srmatrix.SRMatrixFilter;
import com.hkcapital.portflio.repository.tradingindicators.TradingIndicatorsFilter;
import com.hkcapital.portflio.service.registry.Service;

import java.util.List;

public interface TradingIndicatorsService extends Service
{
    TradingIndicator save(TradingIndicator indicators);

    List<TradingIndicator> findByName(String name);

    List<TradingIndicator> findByInstrument(Instrument instrument);
    List<TradingIndicator> findAll();

    TradingIndicator findById(Integer id);
    void remove(TradingIndicator indicators);

    void removeAll();

    List<TradingIndicator> findByFilter(TradingIndicatorsFilter filter);
}
