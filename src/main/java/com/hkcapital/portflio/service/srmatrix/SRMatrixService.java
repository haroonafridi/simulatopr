package com.hkcapital.portflio.service.srmatrix;

import com.hkcapital.portflio.model.Instrument;
import com.hkcapital.portflio.model.SRMatrix;
import com.hkcapital.portflio.service.registry.Service;

import java.util.List;

public interface SRMatrixService extends Service
{
    SRMatrix addSRMatrix(SRMatrix sRMatrix);

    void removeSRMatrix(SRMatrix sRMatrix);

    SRMatrix updateSRMatrix(SRMatrix sRMatrix);

    List<SRMatrix> findAll();

    SRMatrix findById(Integer id);

    void removeById(Integer id);

    void removeAll();

    SRMatrix getReferenceById(Integer id);

    List<SRMatrix> findByTimeFrameAndTimeFrameUnitAndInstrument(Integer timeFrame, String timeFrameUnit,
                                                                Instrument instrument);

}
