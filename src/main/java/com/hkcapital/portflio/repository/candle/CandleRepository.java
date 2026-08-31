package com.hkcapital.portflio.repository.candle;

import com.hkcapital.portflio.model.Candle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CandleRepository extends JpaRepository<Candle, Integer>
{
    List<Candle> findByInstrumentIDAndTimeFrameAndTimeFrameUnitAndCreationDateTimeBetween(
            Integer instrumentID,
            Integer sourceTimeFrame,
            String timeFrameUnit,
            LocalDateTime startDate,
            LocalDateTime endDate
    );

    List<Candle> findByTimeFrameAndTimeFrameUnitAndCreationDateTimeBetween(
            Integer timeFrame,
            String timeFrameUnit,
            LocalDateTime startDate,
            LocalDateTime endDate
    );

    List<Candle> findByInstrumentIDAndCreationDateTimeBetween(
            Integer instrumentID,
            LocalDateTime startDate,
            LocalDateTime endDate);

    List<Candle> findByCreationDateTimeBetween(
            LocalDateTime startDate,
            LocalDateTime endDate);
}
