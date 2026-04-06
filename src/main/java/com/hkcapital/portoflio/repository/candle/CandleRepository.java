package com.hkcapital.portoflio.repository.candle;

import com.hkcapital.portoflio.model.Candle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface CandleRepository extends JpaRepository<Candle, Integer>
{
    List<Candle> findCandleByInstrumentIDAndTimeFrameAndFromDateBetween(Integer instrumentID, String timeFrame, //
                                                            Instant startDate, //
                                                            Instant endDate);
}
