package com.hkcapital.portflio.repository.candle;

import com.hkcapital.portflio.model.Candle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CandleRepository extends JpaRepository<Candle, Integer>
{
//    List<Candle> findCandleByInstrumentIDAndSourceTimeFrameAndTimeFrameUnitFromDateBetween(Integer instrumentID, Integer sourceTimeFrame, //
//                                                                                           String sourceTimeFrameUnit, //
//                                                                                           Instant startDate, //
//                                                                                           Instant endDate);

}
