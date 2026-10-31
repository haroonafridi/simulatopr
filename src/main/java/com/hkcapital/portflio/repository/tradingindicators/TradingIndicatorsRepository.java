package com.hkcapital.portflio.repository.tradingindicators;

import com.hkcapital.portflio.model.Instrument;
import com.hkcapital.portflio.model.SRMatrix;
import com.hkcapital.portflio.model.TradingIndicators;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TradingIndicatorsRepository extends JpaRepository<TradingIndicators, Integer>
{
    List<TradingIndicators> findByName(String name);
    List<TradingIndicators> findByInstrument(Instrument instrument);
}
