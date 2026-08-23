package com.hkcapital.portflio.service.srmatrix;

import com.hkcapital.portflio.model.Instrument;
import com.hkcapital.portflio.model.SRMatrixTolerance;
import com.hkcapital.portflio.repository.srmatrix.SRMatrixToleranceFilter;
import com.hkcapital.portflio.service.registry.Service;

import java.util.List;

public interface SRMatrixToleranceService extends Service
{
    SRMatrixTolerance addSRMatrixTolerance(SRMatrixTolerance sRMatrixTolerance);

    void removeSRMatrixTolerance(SRMatrixTolerance sRMatrixTolerance);

    SRMatrixTolerance updateSRMatrixTolerance(SRMatrixTolerance sRMatrixTolerance);

    List<SRMatrixTolerance> findAll();

    SRMatrixTolerance findById(Integer id);

    SRMatrixTolerance findByInstrumentAndTimeFrameAndTimeFrameUnitAndActive(Instrument instrument, Integer timeFrame, String timeFrameUnit,
                                                                            boolean active);

    void removeById(Integer id);

    void removeAll();

    List<SRMatrixTolerance> findByFilter(SRMatrixToleranceFilter filter);

}
