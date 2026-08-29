package com.hkcapital.portflio.repository.marketconditions;

import com.hkcapital.portflio.model.Instrument;
import com.hkcapital.portflio.model.MarketConditions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MarketConditionsRepository extends JpaRepository<MarketConditions, Integer>
{
    MarketConditions //
    findByInstrumentAndDayLowAndDayHighAndPercentMove(Instrument instrument,
                                                      Double dayLow,
                                                      Double dayHigh,
                                                      Double percentMove);


    MarketConditions //
    findByInstrumentOrderByIdDesc(Instrument instrument);
}
