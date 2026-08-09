package com.hkcapital.portflio.repository.srmatrix;

import com.hkcapital.portflio.model.Instrument;
import com.hkcapital.portflio.model.SRMatrix;
import com.hkcapital.portflio.model.SRMatrixTolerance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface SRMatrixToleranceRepository extends JpaRepository<SRMatrixTolerance, Integer>
{
    SRMatrixTolerance findByInstrumentAndTimeFrameAndTimeFrameUnitAndActive
    (
            Instrument instrument,
            Integer timeFrame,
            String timeFrameUnit,
            boolean active
    );
}
