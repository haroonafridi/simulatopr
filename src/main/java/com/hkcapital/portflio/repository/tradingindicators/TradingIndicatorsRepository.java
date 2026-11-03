package com.hkcapital.portflio.repository.tradingindicators;

import com.hkcapital.portflio.model.Instrument;
import com.hkcapital.portflio.model.TradingIndicator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TradingIndicatorsRepository extends JpaRepository<TradingIndicator, Integer> ,
        JpaSpecificationExecutor<TradingIndicator>
{
    List<TradingIndicator> findByName(String name);
    List<TradingIndicator> findByInstrument(Instrument instrument);
}
