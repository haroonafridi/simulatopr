package com.hkcapital.portflio.repository.srmatrix;

import com.hkcapital.portflio.model.Instrument;
import com.hkcapital.portflio.model.SRMatrix;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SRMatrixRepository extends JpaRepository<SRMatrix, Integer>
{
    List<SRMatrix> findByTimeFrameAndTimeFrameUnitAndInstrument(Integer timeFrame, String timeFrameUnit,
                                                                Instrument instrument);
}
