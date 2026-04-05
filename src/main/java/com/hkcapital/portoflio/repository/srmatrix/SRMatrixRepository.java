package com.hkcapital.portoflio.repository.srmatrix;

import com.hkcapital.portoflio.model.Instrument;
import com.hkcapital.portoflio.model.SRMatrix;
import com.hkcapital.portoflio.model.TradingSessions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SRMatrixRepository extends JpaRepository<SRMatrix, Integer>
{
    List<SRMatrix> findByTimeFrameAndTimeFrameUnitAndInstrument(Integer timeFrame, String timeFrameUnit,
                                                                Instrument instrument);
}
