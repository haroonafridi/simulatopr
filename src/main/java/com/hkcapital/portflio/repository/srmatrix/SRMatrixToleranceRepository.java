package com.hkcapital.portflio.repository.srmatrix;

import com.hkcapital.portflio.model.Instrument;
import com.hkcapital.portflio.model.SRMatrixTolerance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface SRMatrixToleranceRepository extends JpaRepository<SRMatrixTolerance, Integer>, //
        JpaSpecificationExecutor<SRMatrixTolerance>
{
    SRMatrixTolerance findByInstrumentAndTimeFrameAndTimeFrameUnitAndActive
            (
                    Instrument instrument,
                    Integer timeFrame,
                    String timeFrameUnit,
                    boolean active
            );
}
