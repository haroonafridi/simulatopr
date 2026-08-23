package com.hkcapital.portflio.service.srmatrix.impl;

import com.hkcapital.portflio.model.Instrument;
import com.hkcapital.portflio.model.SRMatrix;
import com.hkcapital.portflio.model.SRMatrixTolerance;
import com.hkcapital.portflio.repository.srmatrix.*;
import com.hkcapital.portflio.service.srmatrix.SRMatrixToleranceService;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class SRMatrixToleranceServiceImpl implements SRMatrixToleranceService
{
    private final SRMatrixToleranceRepository sRMatrixToleranceRepository;

    public SRMatrixToleranceServiceImpl(SRMatrixToleranceRepository sRMatrixToleranceRepository)
    {
        this.sRMatrixToleranceRepository = sRMatrixToleranceRepository;
    }

    @Override
    public SRMatrixTolerance addSRMatrixTolerance(SRMatrixTolerance sRMatrixTolerance)
    {
        return sRMatrixToleranceRepository.save(sRMatrixTolerance);
    }

    @Override
    public void removeSRMatrixTolerance(SRMatrixTolerance sRMatrixTolerance)
    {
        sRMatrixToleranceRepository.delete(sRMatrixTolerance);
    }

    @Override
    public SRMatrixTolerance updateSRMatrixTolerance(SRMatrixTolerance sRMatrixTolerance)
    {
        return sRMatrixToleranceRepository.save(sRMatrixTolerance);
    }

    @Override
    public List<SRMatrixTolerance> findAll()
    {
        return sRMatrixToleranceRepository.findAll();
    }

    @Override
    public SRMatrixTolerance findById(Integer id)
    {
        return  sRMatrixToleranceRepository.findById(id).get();
    }

    @Override
    public SRMatrixTolerance findByInstrumentAndTimeFrameAndTimeFrameUnitAndActive(Instrument instrument, Integer timeFrame, String timeFrameUnit, boolean active)
    {
        return sRMatrixToleranceRepository.findByInstrumentAndTimeFrameAndTimeFrameUnitAndActive(instrument,timeFrame,timeFrameUnit,active);
    }


    @Override
    public List<SRMatrixTolerance> findByFilter(SRMatrixToleranceFilter filter)
    {
        return sRMatrixToleranceRepository.findAll(SRMatrixToleranceSpecification.byFilter(filter));
    }

    @Override
    public void removeById(Integer id)
    {
         sRMatrixToleranceRepository.deleteById(id);
    }

    @Override
    public void removeAll()
    {
        sRMatrixToleranceRepository.deleteAll();
    }
}
