package com.hkcapital.portflio.repository.marketconditions;

import com.hkcapital.portflio.model.Instrument;
import com.hkcapital.portflio.model.MarketConditions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MarketConditionsRepository extends JpaRepository<MarketConditions, Integer>
{
    MarketConditions //
    findByInstrumentAndDayLowAndDayHighAndPercentMove(Instrument instrument,
                                                      Double dayLow,
                                                      Double dayHigh,
                                                      Double percentMove);


    List<MarketConditions> //
    findByInstrumentOrderByIdDesc(Instrument instrument);
}
